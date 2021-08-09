/**
 * Node for animations based on an assigned animation function.
 * This function is typically based on the current time
 * Also child elements of the node can be defined with their own respective
 * animations
 */
class AnimationNode extends TransformationSGNode {
    constructor(animation, children) {
        super(mat4.create(), children);
        this.animation = animation;
        this.Node = null;
    }

    /**
     * Render function which sets the matrix returned by the animation function for this element
     * @param {*} context in which to render the scene 
     */
    render(context) {
        super.setMatrix(this.animation(time));
        super.render(context);
    }
}

/**
 * Node for animations based on an assigned animation function and with given coordinates
 * Extends the AnimationNode.
 * The given coordinates can be used as a reference for the animation function to
 * reuse the same animation for objects spawned in different locations.
 */
class PositionAnimationNode extends AnimationNode {
    constructor(animation, children, x, y, z) {
        super(mat4.create(), children);
        this.animation = animation;
        this.Node = null;
        this.x = x;
        this.y = y;
        this.z = z;
        this.jumpStart = Math.random() > 0.5;
        this.jumpSecond = Math.random() > 0.5;
        this.jumpTime = Math.random() * 300;
        this.jumpTimeOffset = Math.random() * 500;
    }
}

/**
 * Used for rendering scene objects which follow the level of detail principle.
 * The closer the camera is to the object, the more details are visible on the screen.
 * Therefore, a higher detail level model has to be used.
 * If the camera is far away, a lower res model takes the place.
 */
class LevelOfDetailNode extends RenderSGNode {
    constructor(low, medium, high, pos, children) {
        super(high, children);
        this.low = low;
        this.medium = medium;
        this.high = high;

        this.pos = pos;
        //Limits based on the render distance between camera and object
        this.limHigh = 8500;
        this.limMed = 22000;
    }

    render(context) {
        //First determines the model to be used,
        //Then calls the render function of RenderSGNode
        this.setPreferedRenderer();
        super.render(context);
    }

    setPreferedRenderer() {
        let dist = vec3.squaredDistance(camera.pos, this.pos);
        
        //If close, set high model
        if (dist <= this.limHigh) {
            super.renderer = this.high;
        } else if (dist <= this.limMed) {
            super.renderer = this.medium;
        } else {
            //When far away, use the low resolution model
            super.renderer = this.low;
        }
    }
}

/**
 * Handles a spotlight with regards to the position, maximum light angle and other properties.
 * sets the different uniforms and attributes needed for calculating the light cone of the spotlight.
 */
class SpotlightNode extends LightSGNode {
    constructor (position, children, direction, cutoffAngle) {
        super(position, children);
        this.direction = direction;
        this.cutoffAngle = cutoffAngle;
    }

    //Sets the additional needed uniforms for a spotlight such as the cutoffAngle and the direction vector
    setShaderUniforms(context) {
        super.setLightUniforms(context);
        super.setLightPosition(context);
        const gl = context.gl;

        //Check if materials are in use
        if (!context.shader || !isValidUniformLocation(gl.getUniformLocation(program, this.uniform+'.cutoffAngle'))
                            || !isValidUniformLocation(gl.getUniformLocation(program, this.uniform+'.direction'))) {
            return; //Immediatly return as there is nothing more to do
        }
        let mv = mat4.multiply(mat4.create(), context.viewMatrix, context.sceneMatrix);
        let n = mat3.normalFromMat4(mat3.create(), mv);
        let dir = vec3.transformMat3(vec3.create(), this.direction, n); 

        gl.uniform1f(gl.getUniformLocation(program, this.uniform+'.cutoffAngle'), Math.cos(glMatrix.toRadian(this.cutoffAngle)));
        gl.uniform3fv(gl.getUniformLocation(program, this.uniform+'.direction'), dir);
    }

    render (context) {
        this.setShaderUniforms(context); //Set the needed uniforms
        super.render(context); //Call the render of LightSGNode that takes care of the rest
    }
}

/**
 * Helps texturing a certain node in the scene
 * Based on the AdvancedTextureSGNode, but also en/disables the u_enableObjectTexture for rendering */
class SimpleTextureNode extends AdvancedTextureSGNode {
    constructor (image, children) {
        super(image, children);
    }

    render (context) {
        gl.uniform1i(gl.getUniformLocation(context.shader, 'u_enableObjectTexture'), 1);
        super.render(context);
        gl.uniform1i(gl.getUniformLocation(context.shader, 'u_enableObjectTexture'), 0);
    }
}

/**
 * Renders the fire particle
 */
class FireParticleNode extends SGNode {
    constructor (particleCount, newParticles, directionFunction) {
        super([]);
        this.particleCount = particleCount;
        this.newParticles = newParticles;
        this.directionFunction = directionFunction;
        this.lastOneUsed = 0;
        this.initEffect();
    }

    /**
     * First inits all the needed elements such as the seed values for more dynamic particle lifetime,
     * the lifetime themselfs (all with the same value) and the direction of the particle
     */
    initEffect() {
        this.seeds = [];
        for (let i=0; i < this.particleCount; i++) {
            this.seeds.push((Math.random() - 0.5)/5.0);
        }
        this.seedBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.seedBuffer);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.seeds), gl.STATIC_DRAW);

        this.lifetimes = [];
        for (let i=0; i < this.particleCount; i++) {
            this.lifetimes.push(0.0);
        }
        this.lifetimeBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.lifetimeBuffer);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.lifetimes), gl.DYNAMIC_DRAW);

        this.directions = [];
        for (let i=0; i < this.particleCount; i++) {
            this.directions.push(0.0, 0.0, 0.0);
        }
        this.directionBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, this.directionBuffer);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.directions), gl.DYNAMIC_DRAW);

        gl.bindBuffer(gl.ARRAY_BUFFER, null);
    }

    //Rebinds the lifetime and direction data to the buffer
    updateParticleData() {
        gl.bindBuffer(gl.ARRAY_BUFFER, this.lifetimeBuffer);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.lifetimes), gl.DYNAMIC_DRAW);

        gl.bindBuffer(gl.ARRAY_BUFFER, this.directionBuffer);
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(this.directions), gl.DYNAMIC_DRAW);

        gl.bindBuffer(gl.ARRAY_BUFFER, null);
    }

    //Updates the direction of a single particle using the passed dir function
    //This function is typically the created cone
    setParticleDirection(index) {
        let direction = this.directionFunction();

        this.directions[index*3] = direction[0];
        this.directions[index*3 + 1] = direction[1];
        this.directions[index*3 + 2] = direction[2];
    }

    decrementLifetime(index) {
        this.lifetimes[index] -= 1/200;
    }

    //Spawns a particle
    spawnParticle() {
        //First decrements its lifetime, then update the direction
        this.decrementLifetime(this.lastOneUsed);
        this.setParticleDirection(this.lastOneUsed);

        //Next checks if the lifetime is already over, if so, reset it
        if (this.lifetimes[this.lastOneUsed] <= 0.0) {
            this.lifetimes[this.lastOneUsed] = 1.0;
        }

        //Increment the last one used and if it is equal or higher
        // to the particle count, restart with the first particle
        this.lastOneUsed++;
        if (this.lastOneUsed >= this.particleCount) {
            this.lastOneUsed = 0;
        }
    }

    //Updates the particles used for creatin the effect
    updateParticles() {
        //First (re)spawns the particles by the given amount
        for (let i=0; i < this.newParticles; i++) {
            this.spawnParticle();
        }

        //Decrease the limetime of all particles which actually have a lifetime
        for (let i=0; i < this.particleCount; i++) {
            if (this.lifetimes[i] > 0.0) {
                this.decrementLifetime(i);
            }
        }
        //Pass the new data in the array buffers
        this.updateParticleData();
    }

    render(context) {
        gl.useProgram(particle_program); //Switch to the particle shader

        this.updateParticles();
        let mView = mat4.multiply(
            mat4.create(),
            context.viewMatrix,
            context.sceneMatrix
        );
        gl.uniformMatrix4fv(gl.getUniformLocation(particle_program, 'u_modelView'), false, mView);
        gl.uniformMatrix4fv(gl.getUniformLocation(particle_program, 'u_projection'), false, context.projectionMatrix);

        gl.enable(gl.BLEND); //Enable alpha blending, i.e. respect the alpha value as well
        gl.enable(gl.DEPTH_TEST); //Enable depth test to not draw lines/surfaces which are hidden by others
        gl.blendFunc(gl.SRC_ALPHA, gl.ONE);
        gl.blendEquation(gl.FUNC_ADD);

        //Attach the calculated attributes to the individual attributes and buffers
        gl.bindBuffer(gl.ARRAY_BUFFER, this.seedBuffer);
        let seedLocation = gl.getAttribLocation(particle_program, 'a_seed');
        if (isValidAttributeLocation(seedLocation)) {
            gl.enableVertexAttribArray(seedLocation);
            gl.vertexAttribPointer(seedLocation, 1, gl.FLOAT, false, 0, 0);
        }

        gl.bindBuffer(gl.ARRAY_BUFFER, this.directionBuffer);
        let directionLocation = gl.getAttribLocation(particle_program, 'a_direction');
        if (isValidAttributeLocation(directionLocation)) {
            gl.enableVertexAttribArray(directionLocation);
            gl.vertexAttribPointer(directionLocation, 3, gl.FLOAT, false, 0, 0);
        }

        gl.bindBuffer(gl.ARRAY_BUFFER, this.lifetimeBuffer);
        let lifetimeLocation = gl.getAttribLocation(particle_program, 'a_lifespan');
        if (isValidAttributeLocation(lifetimeLocation)) {
            gl.enableVertexAttribArray(lifetimeLocation);
            gl.vertexAttribPointer(lifetimeLocation, 1, gl.FLOAT, false, 0, 0);
        }

        gl.drawArrays(gl.POINTS, 0, this.particleCount); //Actually finally draw everything

        gl.disable(gl.BLEND); //Disable alpha blending)
        gl.bindBuffer(gl.ARRAY_BUFFER, null); //Clear the buffer (and the bindings in it)
        gl.useProgram(program); //Switch back to the normal program using the phong shader
    }
}
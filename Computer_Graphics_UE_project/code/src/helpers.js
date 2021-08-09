Helpers = {};

/**
 * Creates a new MaterialSGNode (which itself includes a RenderSGNode) and sets
 * the values for the colors such as ambient, diffuse, specular and shininess.
 * Function was created as this in done quite often and therefore some typeing
 * and potention sources of errors is eliminated
 * @param {*} res The resouce which should be used to create the node
 * @param {*} ambient Ambient color values (array of size 4)
 * @param {*} diffuse Diffuse color values (array of size 4)
 * @param {*} specular Specular color values( array of size 4)
 * @param {*} shininess Shininess value (single term)
 * @returns a new MaterialSGNode which itself has a RenderSGNode with the passed res
 */
Helpers.material = function(res, ambient, diffuse, specular, shininess) {
    let materialNode = new MaterialSGNode([
        new RenderSGNode(res)
    ]);
    materialNode.ambient = ambient;
    materialNode.diffuse = diffuse;
    materialNode.specular = specular;
    materialNode.shininess = shininess;

    return materialNode;
}

// Function to calculate an animations-path based on given target-points and animationtime
// Uses the already integraded bezier function from the framework
Helpers.bezier = function (timeInMilliseconds, startTime, duration, startpos, bez1, bez2, bez3) {
    let bez = vec3.bezier(
        vec3.create(),
        startpos,
        vec3.add(vec3.create(), startpos, bez1),
        vec3.add(vec3.create(), startpos, bez2),
        vec3.add(vec3.create(), startpos, bez3),
        Math.min((timeInMilliseconds - startTime ) / duration, 1)
    );
    return bez;
    //Bezier works like this: https://de.wikipedia.org/wiki/B%C3%A9zierkurve#/media/Datei:Bezier_grad123.svg
}

/**
 * Helper to make the move forward of any given node.
 * Also checks whether the passed not is the ReferenceNode as there also the camera has to be moved forward.
 * Otherwise calls the animation function and returns that result.
 */
Helpers.moveForward = function (timeInMilliseconds, startTime, duration, startpos, bez1, bez2, bez3, anyNode) {    
    let bez =  Helpers.bezier(timeInMilliseconds, startTime, duration, startpos, bez1, bez2, bez3);

    if (anyNode === ReferenceNode) {
        camera.setFocus(bez[0], bez[1], bez[2]);
        camera.movement(timeInMilliseconds);
    }

    return Helpers.animation(bez[0], bez[1], bez[2]);
}

/**
 * Does a simple translation, but could theoretically be extended with other functions
 * such as rotations on certain axis (if not using the rotate functions especially)
 */
Helpers.animation = function (x, y, z) {
    return glm.translate(x, y, z);
}

/**
 * Helper to make a rotation of any given node.
 * First calcs the bezier position and then calls the rotatioAnimation function which results is then returned.
 */
Helpers.rotate = function (timeInMilliseconds, startTime, duration, startpos, bez1, bez2, bez3, anyNode) {
    let bez =  Helpers.bezier(timeInMilliseconds, startTime, duration, startpos, bez1, bez2, bez3);

    return Helpers.rotateAnimation(bez[0], bez[1], bez[2], duration, anyNode);
}

/**
 * Rotates the passed node over the x axis and translates the node to the new position
 */
Helpers.rotateAnimation = function (x,y,z, duration, anyNode) {
    let deltaX = (anyNode.matrix[12] - x) * 400 * (duration/1000);
    return glm.rotateX(deltaX);
}

/**
 * Creates a cone radius (which looks like a rounded pyramid) that can be used
 * later to create the fires in the scene
 * @param {} coneRadius The radius of the cone that is created
 * @returns a vec3 item with the position of the cone
 */
Helpers.createParticleConeFunction = function(coneRadius) {
    return function() {
        let r1 = coneRadius * Math.sqrt(Math.random());
   
        let circumverence = Math.random() *  2 * Math.PI;
        let src = vec3.fromValues(r1 * Math.cos(circumverence), r1 * Math.sin(circumverence), 0);
        let target = vec3.fromValues(0.0, 0, coneRadius + 0.2); //Point where the cone should end
        
        return vec3.subtract(vec3.create(), target, src);
    }
}
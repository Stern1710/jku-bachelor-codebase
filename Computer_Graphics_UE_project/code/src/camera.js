/*
https://learnopengl.com/Getting-started/Camera
implementation of this camera system in c++
ported it to js, because why reinvent the wheel.

added focus feature to be able to track arbitrary points in the scene
added PointerLock to enable infinite yaw/pitch which was previously limited to screen borders
*/

const camera = {};
camera.activated = false; //set with a fixed lock at the start

camera.pos = [5,2,15]; //Initial position
camera.front = [0,0,-1];
camera.up = [0,1,0]; //Up vector
camera.speed = 1.5; //Initial speed

camera.rotation = {pitch: 0, yaw: -90};
camera.deltaTime = 0; //Time difference between current and last frame
camera.lastFrame = 0; //Time of the last framce
camera.focus = [0,0,0]; //Point to focus with the camera
camera.focusStatus = true; //Focus at the beginning

camera.view = mat4.create(); //View matrix, empty at the beginning

camera.addListeners = function() {
    //listeners for keypresses
    document.addEventListener('keypress', function(event) {
        if (event.code === 'Space') { //spacebar
            camera.activated = !camera.activated;
            if(camera.activated) {
                canvas.requestPointerLock();
            } else {
                document.exitPointerLock();
            }
        } 
       
        if (camera.activated && validKey(event.code)) {
            if (event.code === 'KeyW') {
                camera.pos = vec3.add(vec3.create(), camera.pos, mat3.multiplyScalar(vec3.create(), camera.front, camera.speed));
            }
            if (event.code === 'KeyS') {
                camera.pos = vec3.subtract(vec3.create(), camera.pos, mat3.multiplyScalar(vec3.create(), camera.front, camera.speed));
            }
            if (event.code === 'KeyA') {
                camera.pos = vec3.subtract(vec3.create(), camera.pos, mat3.multiplyScalar(vec3.create(),
                    vec3.normalize(vec3.create(), vec3.cross(vec3.create(), camera.front, camera.up)), camera.speed ));
            }
            if (event.code === 'KeyD') {
                camera.pos = vec3.add(vec3.create(), camera.pos, mat3.multiplyScalar(vec3.create(),
                    vec3.normalize(vec3.create(), vec3.cross(vec3.create(), camera.front, camera.up)), camera.speed ));
            }
            if (event.code === 'KeyQ') {
                camera.pos = vec3.subtract(vec3.create(), camera.pos, mat3.multiplyScalar(vec3.create(), camera.up, camera.speed));
            }
            if (event.code === 'KeyE') {
                camera.pos = vec3.add(vec3.create(), camera.pos, mat3.multiplyScalar(vec3.create(), camera.up, camera.speed));
            }
        }
    });

    //mouse listener
    window.addEventListener('mousemove', function(event) {
        if (camera.activated) {
            let sensitivity = 0.05;
            let direction = vec3.create();

            camera.rotation.yaw += event.movementX * sensitivity;
            camera.rotation.pitch -= event.movementY * sensitivity;

            if (camera.rotation.pitch > 89) {
                camera.rotation.pitch = 89;
            }
            if (camera.rotation.pitch < -89) {
                camera.rotation.pitch = -89;
            }

            direction[0] = Math.cos(glm.deg2rad(camera.rotation.yaw)) * Math.cos(glm.deg2rad(camera.rotation.pitch));
            direction[1] = Math.sin(glm.deg2rad(camera.rotation.pitch));
            direction[2] = Math.sin(glm.deg2rad(camera.rotation.yaw)) * Math.cos(glm.deg2rad(camera.rotation.pitch));
            camera.front = vec3.normalize(direction, direction);
            
        } 
    });
}

// Function that sets the flight-path for the automatic camera movement
camera.movement = function(time) {
    //Idea for the camera movement for difference timespots
    //First focus 3 time periods on the beginning of the parade and move slightly back
    //Next 4 time periods, move up and a bit in front of the focus node
    //Use 2 time periods to move back (i.e. negative z axis)
    //5 time periods to move back down
    //1 time period to move a bit more towards the crowd
    //Try to stop at pos z=110, y = 2 or 3, x maybe 0?

    let duration = 2000; 
    if (time <= duration * 3) {
        camera.pos = Helpers.bezier(time, 0, duration*3, vec3.fromValues(0, 3, 15),
                    vec3.fromValues(-15, 0, -2), vec3.fromValues(0, -1, -1), vec3.fromValues(5, -1, 0)); 
    } else if (time <= duration * 7) {
        camera.pos = Helpers.bezier(time, duration*3, duration*4, vec3.fromValues(5, 2, 15),
                     vec3.fromValues(5, 10, 40), vec3.fromValues(0, 50, 60), vec3.fromValues(-5, 133, 75)); 
    } else if (time <= duration * 9) {
        camera.pos = Helpers.bezier(time, duration*7, duration*2, vec3.fromValues(0, 135, 90),
                        vec3.fromValues(-20, 0, -20), vec3.fromValues(-20, 0, -40), vec3.fromValues(0, 0, -60)); 
    } else if (time <= duration * 14) {
        camera.pos = Helpers.bezier(time, duration*9, duration*5, vec3.fromValues(0, 135, 30),
                        vec3.fromValues(-20, -100, 5), vec3.fromValues(20, -115, 15), vec3.fromValues(0, -130, 50)); 
    } else if (time <= duration * 15) {
        camera.pos = Helpers.bezier(time, duration*14, duration, vec3.fromValues(0, 5, 80),
                        vec3.fromValues(0, -1, 7), vec3.fromValues(0, -2, 14), vec3.fromValues(0, -3, 21));     
    }
}

// Update function called in main renderer
camera.update = function() {
    if (camera.focusStatus && !camera.activated) {
        let front = vec3.subtract(vec3.create(), camera.focus, camera.pos);
        camera.front = vec3.normalize(front, front);
    } 
    camera.view = mat4.lookAt(mat4.create(), camera.pos, vec3.add(vec3.create(), camera.pos, camera.front), camera.up);
}

camera.toggelFocus = function() {
    camera.focusStatus = !camera.focusStatus;
}

// sets the camera focus on the given coordinates
camera.setFocus = function(x,y,z) {
    camera.focus[0] = x;
    camera.focus[1] = y;
    camera.focus[2] = z;
} 

//helper functions for checking keycode and calcukating time
function validKey(code) {
    return code === 'KeyW' || code === 'KeyS'
        || code === 'KeyA' || code === 'KeyD'
        || code === 'KeyQ' || code === 'KeyE';
}

function calcTime() {
    let current = time;
    camera.deltaTime = current - camera.lastFrame;
    camera.lastFrame = current;
    return camera.deltaTime;
}


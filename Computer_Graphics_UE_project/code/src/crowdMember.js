CrowdMember = {};

var faca = 0.6;
var facd = 0.65;
var facs = 0.9;
var facav = 0.8;
var facdv = 0.8;
var facsv = 1;

//Different values, all from the rainbow, fot ambient, diffuse, specual values
//They all have a modifier to easily adjust the colour (saved us a lot of headache)
var ambient = [
    [0.8941176*faca, 0.0117647*faca, 0.0117647*faca, 1], // red
    [1.0000000*faca, 0.5490196*faca, 0.0000000*faca, 1], // orange
    [1.0000000*faca, 0.9294118*faca, 0.0000000*faca, 1], // yellow
    [0.0000000*faca, 0.5019608*faca, 0.1490196*faca, 1], // green
    [0.0000000*faca, 0.3019608*faca, 1.0000000*faca, 1], // blue
    [0.4588235*facav, 0.0274510*facav, 0.5294118*facav, 1]  // violet
];

var diffuse = [
    [0.8941176*facd, 0.0117647*facd, 0.0117647*facd, 1], // red
    [1.0000000*facd, 0.5490196*facd, 0.0000000*facd, 1], // orange
    [1.0000000*facd, 0.9294118*facd, 0.0000000*facd, 1], // yellow
    [0.0000000*facd, 0.5019608*facd, 0.1490196*facd, 1], // green
    [0.0000000*facd, 0.3019608*facd, 1.0000000*facd, 1], // blue
    [0.4588235*facdv, 0.0274510*facdv, 0.5294118*facdv, 1]  // violet
];

var specular = [
    [0.8941176*facs, 0.0117647*facs, 0.0117647*facs, 1], // red
    [1.0000000*facs, 0.5490196*facs, 0.0000000*facs, 1], // orange
    [1.0000000*facs, 0.9294118*facs, 0.0000000*facs, 1], // yellow
    [0.0000000*facs, 0.5019608*facs, 0.1490196*facs, 1], // green
    [0.0000000*facs, 0.3019608*facs, 1.0000000*facs, 1], // blue
    [0.4588235*facsv, 0.0274510*facsv, 0.5294118*facsv, 1]  // violet
];

//Creates a single crowd memeber using the PositionAnimationNode
CrowdMember.create = function (resources, xCord, yCord, zCord) {
    let color = Math.floor(Math.random() * 6);
    return new PositionAnimationNode (function(time) {
        let duration = 1000;
        //Use the x/y/z from the node to animate all members of the crowd with the same function, yet different positions
        if (time < duration*2) {
            return Helpers.animation(this.x, this.y, this.z);
        } else if(time < duration*6){ 
            if (this.jumpStart && time >= duration*3 + this.jumpTimeOffset) { //Jump in the beginning
                return Helpers.moveForward(time, duration*3 + this.jumpTimeOffset, duration+this.jumpTime, vec3.fromValues(this.x, this.y, this.z),
                vec3.fromValues(0, 1, 0), vec3.fromValues(0, 1, 0), vec3.fromValues(0, 0, 0), this); 
            }
            return Helpers.animation(this.x, this.y, this.z);
        } else if (time <= duration*16) { //Move forward
            return Helpers.moveForward(time, duration*6, duration*10, vec3.fromValues(this.x, this.y, this.z),
                    vec3.fromValues(0, 0, 20), vec3.fromValues(0, 0, 40), vec3.fromValues(0, 0, 60), this); 
        } else if (time <= duration*17) {
            return Helpers.moveForward(time, duration*16, duration, vec3.fromValues(this.x, this.y, this.z+60),
                    vec3.fromValues(1, 0, 2), vec3.fromValues(1, 0, 4), vec3.fromValues(0, 0, 6), this); 
        } else if (time <= duration*18) {
            return Helpers.moveForward(time, duration*17, duration, vec3.fromValues(this.x, this.y, this.z+66),
                    vec3.fromValues(0, 0, 2), vec3.fromValues(0, 0, 4), vec3.fromValues(0, 0, 6), this); 
        } else if (time <= duration*19) {
            return Helpers.moveForward(time, duration*18, duration, vec3.fromValues(this.x, this.y, this.z+72),
                    vec3.fromValues(-1, 0, 2), vec3.fromValues(-1, 0, 4), vec3.fromValues(0, 0, 6), this); 
        } else if (time <= duration*22) {
            return Helpers.moveForward(time, duration*19, duration*3, vec3.fromValues(this.x, this.y, this.z+78),
            vec3.fromValues(0, 0, 8), vec3.fromValues(0, 0, 12), vec3.fromValues(0, 0, 18), this); 
        } else if (time <= duration*24) {
            if (this.jumpSecond && time <= duration*23) { //Jump in the middle
                return Helpers.moveForward(time, duration*22, duration, vec3.fromValues(this.x, this.y, this.z+96),
                vec3.fromValues(0, 1, 2), vec3.fromValues(0, 1, 4), vec3.fromValues(0, 0, 6), this);
            } else {
                return Helpers.moveForward(time, duration*22, duration*2, vec3.fromValues(this.x, this.y, this.z+96),
                    vec3.fromValues(0, 0, 4), vec3.fromValues(0, 0, 8), vec3.fromValues(0, 0, 12), this);
            }
        } else if (time <= duration*30) {
            return Helpers.moveForward(time, duration*24, duration*6, vec3.fromValues(this.x, this.y, this.z+108),
                    vec3.fromValues(0, 0, 10), vec3.fromValues(0, 0, 20), vec3.fromValues(0, 0, 30), this); 
        } else if (time <= duration*32) { //BONUS JUMP! Now 6% more video time, limited time offer only!
            if (this.jumpStart && time >= duration*30 + this.jumpTimeOffset) { //Final jump 
                return Helpers.moveForward(time, duration*30 + this.jumpTimeOffset, duration+this.jumpTime, vec3.fromValues(this.x, this.y, this.z+138),
                vec3.fromValues(0, 1, 0), vec3.fromValues(0, 1, 0), vec3.fromValues(0, 0, 0), this); 
            }
            return this.matrix;
        }

        return this.matrix;
    }, [ //Load the crowdMember as a material and slightly varying scale for each one
        new TransformationSGNode(glm.transform({
            scale: Math.random() * 0.1 + 0.65
        }), [
            Helpers.material(
                resources.crowdMember,
                ambient[color],
                diffuse[color],
                specular[color],
                40
            )
        ])
        
        ],
        xCord, //Pass the starting coordinates; Will be used for the animation
        yCord,
        zCord
    );
}
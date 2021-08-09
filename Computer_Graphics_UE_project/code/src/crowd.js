
CrowdFunc = {};

let xBound = 10, zBound = -24; //xBound is +-value, zBound is from 0 .. zBound

/**
 * Creates all the crowd members and attaches them to the root node.
 * The x and z position are slightly different for each member to make the scene more organic
 * @param {*} resources Resource object which holds all loaded resources such as obj files
 * @param {*} rootNode The root node of the scene
 */
CrowdFunc.initCrowd = function (resources, rootNode) {
    for (i = -xBound; i <= xBound; i += 2) {
        for (j = -4; j >= zBound; j -= 2) {
            rootNode.append( //Append the created member to the scene
                CrowdMember.create( //Create a single cloud member
                    resources, 
                    i + 1.1*(Math.random() - 0.5), 
                    0, 
                    j + 1.2*(Math.random() - 0.5) - (j/zBound) //Gets a bit more random the closer to the end the people are
                )
            );
        }
    }
}

/**
 * Creates the four torch holders which stand next to the street and hold up
 * a torch which itself is implementing the particle effect
 * @param {*} resources Resource object which holds all loaded resources such as obj files
 * @param {*} rootNode The root node of the scene
 */
CrowdFunc.initTorchHolders = function(resources, root) {
    let fireNode = new TransformationSGNode( //Create the fireNode and add it to the scene
        glm.transform({
          rotateX: 90,
          translate: [0, 0.6, 0]
        }), [
          new FireParticleNode(3000, 10, Helpers.createParticleConeFunction(0.2))
        ]
    );
    
    let torch = Helpers.material(
        resources.torch,
        [0.58, 0.29, 0.0, 1],
        [0.58, 0.29, 0.0, 1],
        [0.58, 0.29, 0.0, 1],
        5
    );
    
    torch.append(fireNode); //Add the fire on top of the torch
    
    let torchNode = new TransformationSGNode( //Place the torch in the hand of the person (loaded next)
        glm.transform({
        translate: [1.4, 2.2, 0.35],
        scale: 1.0
        }), [
            torch
        ]
    );

    let personWithTorch = Helpers.material( //Create the person which should hold the torch
        resources.crowdMember,
        [0.24725, 0.1995, 0.0745, 1],
        [0.75164, 0.60648, 0.22648, 1],
        [0.628281, 0.555802, 0.366065, 1],
        50
    );

    personWithTorch.append(torchNode); //Append torch with fire effect to the person

    //Place four people with a torch to the scene
    root.append(new TransformationSGNode(
        glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
        translate: [-25, 0, 10],
        rotateY: 90,
        scale: 0.9
        }), [
            personWithTorch
        ]
    ));

    root.append(new TransformationSGNode(
        glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
        translate: [-25, 0, 110],
        rotateY: -90,
        scale: 0.9
        }), [
            personWithTorch
        ]
    ));

    root.append(new TransformationSGNode(
        glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
        translate: [25, 0, 10],
        rotateY: 90,
        scale: 0.9
        }), [
            personWithTorch
        ]
    ));

    root.append(new TransformationSGNode(
        glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
        translate: [25, 0, 110],
        rotateY: -90,
        scale: 0.9
        }), [
            personWithTorch
        ]
    ));
}

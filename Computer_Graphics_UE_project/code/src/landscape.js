const landscape = {};

landscape.nodes = new SGNode();
landscape.lights = new SGNode();

//Overall, the landscape should be z=120 long; x= +-60
initLandscape = function (resources) {

    //Create far side (landscape with texture and buildings)
    {
        //Street on the far side
        var rect = makeRect(10, 20);
        rect.texture = [0, 0 /**/, 2, 0 /**/, 2, 4 /**/, 0, 4];
        let riverFarSideRoad = Helpers.material(
            rect,
            [0.7, 0.7, 0.7, 1],
            [0.3, 0.3, 0.3, 1],
            [0.2, 0.2, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [0,0,0],
            rotateX: -90,
            scale: 2
            }), [
                new SimpleTextureNode(
                    resources.asphalt_texture, 
                    [
                        riverFarSideRoad
                    ]
                )
            ]
        ));
        
        //Gras landscape on the left
        var rect = makeRect(20, 20);
        rect.texture = [0, 0 /**/, 6, 0 /**/, 6, 6 /**/, 0, 6];
        let riverFarSideLeft = Helpers.material(
            rect,
            [0.4, 0.6, 0.4, 1],
            [0.1, 0.3, 0.1, 1],
            [0.2, 0.2, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [-60,0,0],
            rotateX: -90,
            scale: 2
            }), [
                new SimpleTextureNode(
                    resources.gras_texture, 
                    [
                        riverFarSideLeft
                    ]
                )
            ]
        ));
        landscape.nodes.append(createLodBuildung(-40, 0, 20, 0, resources));

        landscape.nodes.append(new TransformationSGNode(
            glm.transform({
                translate: [-35, 0, 25],
                rotateY: 90
            }), [
                Helpers.material(
                    resources.c3po,
                    [0.24725, 0.1995, 0.0745, 1],
                    [0.75164, 0.60648, 0.22648, 1],
                    [0.628281, 0.555802, 0.366065, 1],
                    50
                )
            ]
        ));

        //Gras landscape on the side
        var rect = makeRect(20, 20);
        rect.texture = [0, 0 /**/, 6, 0 /**/, 6, 6 /**/, 0, 6];
        let riverFarSideRight = Helpers.material(
            rect,
            [0.4, 0.6, 0.4, 1],
            [0.1, 0.3, 0.1, 1],
            [0.2, 0.2, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [60,0,0],
            rotateX: -90,
            scale: 2
            }), [
                new SimpleTextureNode(
                    resources.gras_texture, 
                    [
                        riverFarSideRight
                    ]
                )
            ]
        ));
        landscape.nodes.append(createLodBuildung(40, 0, 20, -90, resources));
    }

    //Create bridge, river and channel
    {
        //Create the blue danube
        var rect = makeRect(100, 20);
        rect.texture = [0, 0 /**/, 5, 0 /**/, 5, 1 /**/, 0, 1];
        let river = Helpers.material(
            rect,
            [0.2, 0.5, 0.9, 1],
            [0, 0, 0, 1],
            [0, 0, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({
            translate: [0,-4.5,60],
            rotateX: -90,
            scale: 1
            }), [
                new SimpleTextureNode(
                    resources.water_texture, 
                    [
                        river
                    ]
                )
            ]
        ));
        //Create the bridge
        let bridge = Helpers.material(
            resources.bridge,
            [0.4, 0.4, 0.4, 1],
            [0.5, 0.5, 0.5, 1],
            [0.2, 0.2, 0.2, 1],
            15
        );    
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({
                translate: [0, -3.28, 60],
                rotateX: 0.15,
                rotateY: 90,
                scale: 1.571
                }), [
                    bridge
                ]
        ));

        // Walls of both side of the bridge to cover the height difference
        var rect = makeRect(100, 2.3)
        rect.texture = [0, 0 /**/, 25, 0 /**/, 25, 0.5 /**/, 0, 0.5];
        let riverFarSideWall = Helpers.material(
            rect,
            [1, 1, 1, 1],
            [0.3, 0.3, 0, 1],
            [0.4, 0.4, 0.4, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [0,-2.3,40],
            scale: 1
            }), [
                new SimpleTextureNode(resources.wall_texture, [
                    riverFarSideWall
                ])
            ]
        ));

        var rect = makeRect(100, 2.3)
        rect.texture = [0, 0 /**/, 25, 0 /**/, 25, 0.5 /**/, 0, 0.5];
        let riverNearSideWall = Helpers.material(
            rect,
            [1, 1, 1, 1],
            [0, 0, 0, 1],
            [0.2, 0.2, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({
            translate: [0,-2.3,80],
            rotateX: 180,
            scale: 1
            }), [
                new SimpleTextureNode(resources.wall_texture, [
                    riverNearSideWall
                ])
            ]
        ));                
    }

    //Create near side (landscape with texture and buildings)
    {
        //Street on the near side
        var rect = makeRect(10, 20);
        rect.texture = [0, 0 /**/, 2, 0 /**/, 2, 4 /**/, 0, 4];
        let riverNearSideRoad = Helpers.material(
            rect,
            [0.7, 0.7, 0.7, 1],
            [0.7, 0.7, 0.7, 1],
            [0.3, 0.3, 0.3, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({
            translate: [0,0,120],
            rotateX: -90,
            scale: 2
            }), [
                new SimpleTextureNode(
                    resources.asphalt_texture, 
                    [
                        riverNearSideRoad
                    ]
                )
            ]
        ));

        //Gras landscape on the left
        var rect = makeRect(20,20);
        rect.texture = [0, 0 /**/, 6, 0 /**/, 6, 6 /**/, 0, 6];
        let riverNearSideLeft = Helpers.material(
            rect,
            [0.5, 0.9, 0.5, 1],
            [0.5, 0.8, 0.3, 1],
            [0.2, 0.2, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [-60,0,120],
            rotateX: -90,
            scale: 2
            }), [
                new SimpleTextureNode(
                    resources.gras_texture, 
                    [
                        riverNearSideLeft
                    ]
                )
            ]
        ));
        landscape.nodes.append(createLodBuildung(-40, 0, 100, 0, resources));

        //Gras landscape on the right
        var rect = makeRect(20,20);
        rect.texture = [0, 0 /**/, 6, 0 /**/, 6, 6 /**/, 0, 6];
        let riverNearSideRight = Helpers.material(
            rect,
            [0.5, 0.9, 0.5, 1],
            [0.5, 0.8, 0.3, 1],
            [0.2, 0.2, 0.2, 1],
            5
        );
        landscape.nodes.append(new TransformationSGNode(
            glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [60,0,120],
            rotateX: -90,
            scale: 2
            }), [ 
                new SimpleTextureNode(
                    resources.gras_texture, 
                    [
                        riverNearSideRight
                    ]
            )
        ]
        ));
        landscape.nodes.append(createLodBuildung(40, 0, 100, 180, resources));
    }

    //Create the clouds and the trees
    {
        //Spawns the clouds
        createClouds(resources);

        //Spawn trees
        createTrees(resources, -80, -20, -28, 2);   //Far left side back
        createTrees(resources, -80, -50, 10, 40);   //Far left side front
        createTrees(resources, 40, 100,  -28, 2);   //Far right side back
        createTrees(resources, 70, 100,  20, 30);   //Far right side front

        createTrees(resources, -80, -50, 90, 110); //Near left side back
        createTrees(resources, -80, -30, 130, 160); //Near left side front
        createTrees(resources, 80, 100, 90, 110); //Near right side back
        createTrees(resources, 60, 100, 130, 160);  //Near right side front
    }
}

/**
 * Creates the twelve spotlight sources on the bridge
 * as SpotlightNodes.
 */
 initLandscapeLighting = function () {
    let dirLeft =  vec3.normalize(vec3.create(),vec3.subtract(vec3.create(), vec3.fromValues(9.5,0,0), vec3.fromValues(12.3,5,0)));
    let dirRight =  vec3.normalize(vec3.create(),vec3.subtract(vec3.create(), vec3.fromValues(-9.5,0,0), vec3.fromValues(-12.3,5,0)));

    let spotlight0 = new SpotlightNode();
    spotlight0.ambient = [0, 0, 0, 1];
    spotlight0.diffuse = [1, 0, 0.09, 0.25];
    spotlight0.specular = [0, 0, 0, 0];
    spotlight0.position = [12.3, 5, 46.5];
    spotlight0.direction = dirLeft;
    spotlight0.cutoffAngle = 30;
    spotlight0.uniform = 'u_spotlight0';
    landscape.lights.append(spotlight0);
    
    let spotlight1 = new SpotlightNode();
    spotlight1.ambient = [0, 0, 0, 1];
    spotlight1.diffuse = [1, 0.64, 0.17, 0.25]
    spotlight1.specular = [0, 0, 0, 0];
    spotlight1.position = [12.3, 5, 52];
    spotlight1.direction = dirLeft;
    spotlight1.cutoffAngle = 30;
    spotlight1.uniform = 'u_spotlight1';
    landscape.lights.append(spotlight1);

    let spotlight2 = new SpotlightNode();
    spotlight2.ambient = [0, 0, 0, 1];
    spotlight2.diffuse = [1, 1, 0, 0.25];
    spotlight2.specular = [0, 0, 0, 0];
    spotlight2.position = [12.3, 5, 57.75];
    spotlight2.direction = dirLeft;
    spotlight2.cutoffAngle = 30;
    spotlight2.uniform = 'u_spotlight2';
    landscape.lights.append(spotlight2);

    let spotlight3 = new SpotlightNode();
    spotlight3.ambient = [0, 0, 0, 1];
    spotlight3.diffuse = [0, 0.5, 0.09, 0.25];
    spotlight3.specular = [0, 0, 0, 0];
    spotlight3.position = [12.3, 5, 63.25];
    spotlight3.direction = dirLeft;
    spotlight3.cutoffAngle = 30;
    spotlight3.uniform = 'u_spotlight3';
    landscape.lights.append(spotlight3);

    let spotlight4 = new SpotlightNode();
    spotlight4.ambient = [0, 0, 0, 1];
    spotlight4.diffuse = [0, 0, 0.97, 0.25];
    spotlight4.specular = [0, 0, 0, 0];
    spotlight4.position = [12.3, 5, 68.75];
    spotlight4.direction = dirLeft;
    spotlight4.cutoffAngle = 30;
    spotlight4.uniform = 'u_spotlight4';
    landscape.lights.append(spotlight4);

    let spotlight5 = new SpotlightNode();
    spotlight5.ambient = [0, 0, 0, 1];
    spotlight5.diffuse = [0.52, 0, 0.49, 0.25];
    spotlight5.specular = [0, 0, 0, 0];
    spotlight5.position = [12.3, 5, 74];
    spotlight5.direction = dirLeft;
    spotlight5.cutoffAngle = 30;
    spotlight5.uniform = 'u_spotlight5';
    landscape.lights.append(spotlight5);

    let spotlight6 = new SpotlightNode();
    spotlight6.ambient = [0, 0, 0, 1];
    spotlight6.diffuse = [1, 0, 0.09, 0.25];
    spotlight6.specular = [0, 0, 0, 0];
    spotlight6.position = [-12.3, 5, 46.5];
    spotlight6.direction = dirRight;
    spotlight6.cutoffAngle = 30;
    spotlight6.uniform = 'u_spotlight6';
    landscape.lights.append(spotlight6);

    let spotlight7 = new SpotlightNode();
    spotlight7.ambient = [0, 0, 0, 1];
    spotlight7.diffuse = [1, 0.64, 0.17, 0.25]
    spotlight7.specular = [0, 0, 0, 0];
    spotlight7.position = [-12.3, 5, 52];
    spotlight7.direction = dirRight;
    spotlight7.cutoffAngle = 30;
    spotlight7.uniform = 'u_spotlight7';
    landscape.lights.append(spotlight7);

    let spotlight8 = new SpotlightNode();
    spotlight8.ambient = [0, 0, 0, 1];
    spotlight8.diffuse = [1, 1, 0, 0.25];
    spotlight8.specular = [0, 0, 0, 0];
    spotlight8.position = [-12.3, 5, 57.75];
    spotlight8.direction = dirRight;
    spotlight8.cutoffAngle = 30;
    spotlight8.uniform = 'u_spotlight8';
    landscape.lights.append(spotlight8);

    let spotlight9 = new SpotlightNode();
    spotlight9.ambient = [0, 0, 0, 1];
    spotlight9.diffuse = [0, 0.5, 0.09, 0.25];
    spotlight9.specular = [0, 0, 0, 0];
    spotlight9.position = [-12.3, 5, 63.25];
    spotlight9.direction = dirRight;
    spotlight9.cutoffAngle = 30;
    spotlight9.uniform = 'u_spotlight9';
    landscape.lights.append(spotlight9);

    let spotlight10 = new SpotlightNode();
    spotlight10.ambient = [0, 0, 0, 1];
    spotlight10.diffuse = [0, 0, 0.97, 0.25];
    spotlight10.specular = [0, 0, 0, 0];
    spotlight10.position = [-12.3, 5, 68.75];
    spotlight10.direction = dirRight;
    spotlight10.cutoffAngle = 30;
    spotlight10.uniform = 'u_spotlight10';
    landscape.lights.append(spotlight10);

    let spotlight11 = new SpotlightNode();
    spotlight11.ambient = [0, 0, 0, 1];
    spotlight11.diffuse = [0.52, 0, 0.49, 0.25];
    spotlight11.specular = [0, 0, 0, 0];
    spotlight11.position = [-12.3, 5, 74];
    spotlight11.direction = dirRight;
    spotlight11.cutoffAngle = 30;
    spotlight11.uniform = 'u_spotlight11';
    landscape.lights.append(spotlight11);
}

/**
 * Spawns clouds all over the place.
 * Check that the coordinates are not straight above the bridge.
 * If they are, decrease the counter variable by 1 and go to the next iteration.
 * Otherwise spawn the cloud with given coordinates.
 */
createClouds = function(resources) {
    let numClouds = 150;
    let cloudsHeight = 55;
    let cloudSubstract = 15;

    //Create clouds semi-randomly in the sky
    let singleCloud = new MaterialSGNode([
        new RenderSGNode(resources.cloud)
    ]);
    singleCloud.ambient = [0.25, 0.25, 0.30, 0.922];
    singleCloud.diffuse = [0.80, 0.80, 0.8, 0.922];
    singleCloud.specular = [0.296648, 0.296648, 0.296648, 0.922];
    singleCloud.shininess = 11;

    //Create thespecified amount of clouds semi-randomly in the sky
    for (i=0; i < numClouds; i++) {
        var cloudX = Math.random() * 190 - 95;
        var cloudY = cloudsHeight - Math.random() * cloudSubstract;
        var cloudZ = Math.random() * 200 - 30;

        //Check if coordinates are over the bridge
        if (cloudX < 20 && cloudX > -20 && cloudZ < 90 && cloudZ > 40) {
            i--;
        } else {
            let transCloud = new TransformationSGNode(glm.transform({
                translate: [cloudX, cloudY, cloudZ],
                scale: (Math.random() + 3.0),
                rotateY: 90
            }), [
                singleCloud
            ]);

            landscape.nodes.append(transCloud);
        }
    }
}

/**
 * Creates the Level of Detail building with given coordinates
 * and rotation around the y-axis as a MaterialSGNode (which itself
 * has a transformation and LODNode as children)
 */
createLodBuildung = function (x, y, z, yRotate, resources) {
    let high = modelRenderer(resources.building_high);
    let medium = modelRenderer(resources.building_medium);
    let low = modelRenderer(resources.building_low);

    let building = new MaterialSGNode([
        new TransformationSGNode(glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
            translate: [x,y,z],
            rotateY: yRotate,
            scale: 1.5
            }), [
            new LevelOfDetailNode(low, medium, high, [x,y,z], [])
        ])
    ]);
    building.ambient = [0.39, 0.22, 0.12, 1]; 
    building.diffuse = [0.65, 0.42, 0.22, 1];
    building.specular = [0, 0, 0, 1];
    building.shininess = 9;

    return building;
}

/**
 * Creates trees in a 10x10 grid with varying spawning positions withing this grid.
 * The grid is specified by the lower/upper limits
 */
createTrees = function (resources, xLowerLim, xUpperLim, zLowerLim, zUpperLim) {
    let singleTree = Helpers.material(
        resources.tree,
        [0.3, 0.4, 0.3, 1],
        [0.05, 0.15, 0.0, 1],
        [0.1, 0.2, 0.1, 1],
        100
    );

    for (i = xLowerLim; i <= xUpperLim; i += 10) {
        for (j = zLowerLim; j <= zUpperLim; j += 10) {
            let tree = new TransformationSGNode(glm.transform({
                translate: [i + 5*(Math.random() - 2.5),
                    0,
                    j + 4*(Math.random() - 2.0)
                ],
                scale: 1.5
            }), [
                singleTree
            ]);

            landscape.nodes.append(tree);
        }
    }
}
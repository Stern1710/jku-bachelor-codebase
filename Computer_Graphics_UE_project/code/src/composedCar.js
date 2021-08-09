
ComposedCar = {};

ComposedCar.create = function(resources) {
    model = Helpers.material(
        resources.car,
        [0.9, 0, 0.49, 1],
        [0.9, 0.07, 0.49, 1],
        [0.628281, 0.555802, 0.366065, 1],
        50
    );

    let spotlightMov1 = new SpotlightNode();
    spotlightMov1.ambient = [0, 0, 0, 1];
    spotlightMov1.diffuse = [1, 1, 0.3, 1];
    spotlightMov1.specular = [0, 0, 0, 0];
    spotlightMov1.position = [3, 5.75, 1];
    spotlightMov1.direction = [0, -5, 5];
    spotlightMov1.cutoffAngle = 20;
    spotlightMov1.uniform = 'u_spotlightMov1';
    model.append(spotlightMov1);

    let spotlightMov2 = new SpotlightNode();
    spotlightMov2.ambient = [0, 0, 0, 1];
    spotlightMov2.diffuse = [1, 1, 0.3, 1];
    spotlightMov2.specular = [0, 0, 0, 0];
    spotlightMov2.position = [-3, 5.75, 1];
    spotlightMov2.direction = [0, -5, 5];
    spotlightMov2.cutoffAngle = 20;
    spotlightMov2.uniform = 'u_spotlightMov2';
    model.append(spotlightMov2);


    //add the windows of the car
    model.append(
        Helpers.material(
            resources.car_windows,
            [0.05, 0.05, 0.05, 1],
            [0.01, 0.01, 0.01, 1],
            [0.55, 0.55, 0.55, 1],
            50                                      
    ));

    //add the lights of the car
    model.append(
        Helpers.material(
            resources.car_lights,
            [0.5, 0.5, 0.2, 1],
            [0.9, 0.9, 0.3, 1],
            [0.628281, 0.555802, 0.366065, 1],
            100                                      
    ));

    //add the mirrors of the car
    model.append(
        Helpers.material(
            resources.car_mirrors,
            [0.22, 0.22, 0.22, 1],
            [0.9, 0.9, 0.9, 1],
            [1, 1, 1, 1],
            50                                      
    ));

    //add the bumpers of the car
    model.append(
        Helpers.material(
            resources.car_bumpers,
            [0.22, 0.22, 0.22, 1],
            [0.77, 0.78, 0.78, 1],
            [1, 1, 1, 1],
            25                                      
    ));

    //Load tires axis
    let tiresAxis = Helpers.material(
        resources.tiresAxis,
        [0.2, 0.2, 0.2, 1],
        [0.05, 0.05, 0.05, 1],
        [0, 0, 0, 1],
        0
    );

    //Animation of the tires axis
    let myAnimation = function(time) {
        let duration = 1000; 

        if (time <= duration*30) {
            return Helpers.rotate(time, duration*0, duration*30, vec3.fromValues(0, 0, 0),
                    vec3.fromValues(0.2, 0, 0), vec3.fromValues(0.4, 0, 0), vec3.fromValues(0.6, 0, 0), this); 
        }
    return this.matrix;
    };

    //Append front and back axis to model so the tires move with the car
    model.append(
        new TransformationSGNode(glm.transform({
            translate: [0.08, -0.25, -4.3],
            scale: 1.25,
            rotateY: 180
            }), [
            new AnimationNode(
                myAnimation, [
                    tiresAxis
                ]
            )
        ])
    );
    model.append(
        new TransformationSGNode(glm.transform({
            translate: [0.08, -0.25, 1],
            scale: 1.25,
            rotateY: 180
            }), [
            new AnimationNode(
                myAnimation, [
                    tiresAxis
                ]
            )
        ])
    );

    return new AnimationNode (function(time) {
        let duration = 1000;
        // movements of the main objects are calculated here based on the rendertime
        if (time <= duration*6) { 
           return Helpers.moveForward(time, 0, duration*6, vec3.fromValues(0, 0.75, 3),
                    vec3.fromValues(0, 0, 0), vec3.fromValues(0, 0, 0), vec3.fromValues(0, 0, 0), this);
        } else if (time <= duration*30) {
            return Helpers.moveForward(time, duration*6, (duration*24), vec3.fromValues(0, 0.75 , 3),
                    vec3.fromValues(0, 0, 49), vec3.fromValues(0, 0, 98), vec3.fromValues(0, 0, 147), this); 
        }

        return this.matrix;
    }, [
        model
    ]);
}
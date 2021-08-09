fish = {};

fish.nodes = new SGNode();

/**
 * Spawns 3-4 fish in total and place them somewhere over the water
 */
initFish = function(resources) {
    let fishNode = createFish(resources); //Create the fish

    fish.nodes.append(new TransformationSGNode(
      glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
      translate: [-70,-4.2, 55],
      rotateY: 145,
      scale: 2.5
      }), [
        fishNode
      ]
    ));

    fish.nodes.append(new TransformationSGNode(
      glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
      translate: [-30,-4.2, 70],
      rotateY: 230,
      scale: 1.8
      }), [
        fishNode
      ]
    ));

    fish.nodes.append(new TransformationSGNode(
      glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
      translate: [40,-4.2, 50],
      rotateY: 120,
      scale: 2.0
      }), [
        fishNode
      ]
    ));

    fish.nodes.append(new TransformationSGNode(
      glm.transform({ //First scale, then rotate, then translate; Read from bottom to top
      translate: [60,-4.2, 65],
      rotateY: 260,
      scale: 2.3
      }), [
        fishNode
      ]
    ));

}

/**
 * Creates a single fish
 */
createFish = function(resources) {
    let fish = Helpers.material(
        createFishProperties(), //Creaate the fish
        [0.7, 0.7, 0.7, 0],
        [0.5, 0.3, 0.5, 0],
        [0, 0, 0, 0],
        20
    );

    return new SimpleTextureNode( //Add the texture on the fish
        resources.fish_texture,
        fish
    );
}

/**
 * Creates the fish by specifying all the properties such as
 * the vertices, the triangles (using the vertices), texture coordinates and the normals
 */
createFishProperties = function() {
    //Some helper values for easier handling of data
    let textureSize = 2.0;
    //Arrays which will be filled with their respective data
    let positions = [];
    let indexes = [];
    let normals = [];
    let textures = [];

    //Main fish body
    let v0 = new SingleVertex(-1.5, 0.0, 0.0);
    let v1 = new SingleVertex(0.0, 0.5, 0.0);
    let v2 = new SingleVertex(1.0, 0.3, 0.0);
    let v3 = new SingleVertex(0.0, 0.0, 0.6);
    let v4 = new SingleVertex(0.0, 0.0, -0.6);
    let v5 = new SingleVertex(2.5, 0.0, 0.0);
    //fins
    let v6 = new SingleVertex(0.5, 0.3, 1.5);
    let v7 = new SingleVertex(-0.5, 0.2, 1.5);
    let v8 = new SingleVertex(0.5, 0.3, -1.5);
    let v9 = new SingleVertex(-0.5, 0.2, -1.5);
    let v10 = new SingleVertex(3.5, 0.1, -1.0);
    let v11 = new SingleVertex(3.5, 0.1, 1.0);
    let v12 = new SingleVertex(0.75, 0.6, 0.0);

    //Add all vertice
    {
        positions.pushSingleVertex(v0);
        positions.pushSingleVertex(v1);
        positions.pushSingleVertex(v2);
        positions.pushSingleVertex(v3);
        positions.pushSingleVertex(v4);
        positions.pushSingleVertex(v5);
        positions.pushSingleVertex(v6);
        positions.pushSingleVertex(v7);
        positions.pushSingleVertex(v8);
        positions.pushSingleVertex(v9);
        positions.pushSingleVertex(v10);
        positions.pushSingleVertex(v11);
        positions.pushSingleVertex(v12);
    }

    //Set indexes for triangle mesh structure (which vertices to use)
    {
        //Main body
        indexes.push(0, 1, 3);
        indexes.push(0, 1, 4);
        indexes.push(0, 3, 4);
        indexes.push(1, 3, 2);
        indexes.push(1, 4, 2);
        indexes.push(2, 3, 5);
        indexes.push(2, 4, 5);
        indexes.push(3, 4, 5);

        //Fins
        indexes.push(3, 6, 7);
        indexes.push(4, 8, 9);
        indexes.push(5, 10, 11);
        indexes.push(1, 2, 12);
    }

    //Textures everywhere!
    {
        textures.push(0.0, 0.5*textureSize); //v0
        textures.push(0.5*textureSize, 1.0*textureSize);
        textures.push(0.5*textureSize, 1.5*textureSize);
        textures.push(0.2*textureSize, 1.0*textureSize);
        textures.push(0.8*textureSize, 1.0*textureSize);
        textures.push(0.5*textureSize, 1.0*textureSize); //v5
        textures.push(0.6*textureSize,0.0*textureSize);
        textures.push(0.4*textureSize,0.0*textureSize);
        textures.push(0.6*textureSize,0.0*textureSize);
        textures.push(0.4*textureSize,0.0*textureSize); //v9
        textures.push(0.5*textureSize, 0.5*textureSize);
        textures.push(-0.5*textureSize, 0.5*textureSize);
        textures.push(0.7*textureSize, 1.0*textureSize);
    }

    //Specify the normals for the vertices for proper lighting effects
    //Some normals will be calculated, some will be handdesigned (where multiple sides share a vertex)
    {
        normals.push(0.3, 1.0, 0.0); //v0
        normals.push(0.0, 1.0, 0.0);
        normals.push(0.0, 1.0, 0.0);
        normals.push(0.0, 1.0, 0.5);
        normals.push(0.0, 1.0, -0.5);
        normals.push(1.0, 1.0, 0.0); //v5

        myNormal = Normals.getNormals([v6, v3, v7]);
        normals = normals.concat(myNormal);
        normals = normals.concat(myNormal);

        myNormal = Normals.getNormals([v4, v8, v9]);
        normals = normals.concat(myNormal);
        normals = normals.concat(myNormal);

        myNormal = Normals.getNormals([v10, v4, v11]);
        normals = normals.concat(myNormal);
        normals = normals.concat(myNormal);

        normals.push(0.0, 1.0, 0.0);
    }

    //Return a data structure that has all the important information in it
    return {
        position: positions,
        normal: normals,
        texture: textures,
        index: indexes,
    };
}

/**
 * This is a helper class to handle the vertices of our handcrafted object
 */
class SingleVertex {
    constructor(xCoord, yCoord,  zCoord) {
        this.x = xCoord;
        this.y = yCoord;
        this.z = zCoord;
    }

    //Returns the x/y/z coordiante
    getXCoord() {
        return this.x;
    }
    getYCoord() {
        return this.y;
    }
    getZCoord() {
        return this.z;
    }

    //Return the coordinates in a array of size 3
    getCoordsAsArray() {
        return [this.x, this.y, this.z];
    }

    //Subtract two coordinates from teach other
    //Each value from vertex1 is substracted from vertex2
    //and returned as a new SingleVertex
    static subCoordinates (vertex1, vertex2) {
        return new SingleVertex(
            vertex1.getXCoord() - vertex2.getXCoord(),
            vertex1.getYCoord() - vertex2.getYCoord(),
            vertex1.getZCoord() - vertex2.getZCoord()
        );
    }
}

/**
 * Helper class for normal handling
 */
class Normals {
    /**
     * Calculates the normals for each of the three vertices passed (as an array)
     * and returns them
     * Based on the answer from
     * https://stackoverflow.com/questions/19350792/calculate-normal-of-a-single-triangle-in-3d-space
     * https://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
     * @param {*} vertices - Is an arry of three vertices three times (for each vertex)
     */
    static getNormals(vertices) {
        let u = SingleVertex.subCoordinates(vertices[1], vertices[0]);
        let v = SingleVertex.subCoordinates(vertices[2], vertices[0]);
        let x = u.getYCoord()*v.getZCoord() - u.getZCoord()*v.getYCoord();
        let y = u.getZCoord()*v.getXCoord() - u.getXCoord()*v.getZCoord();
        let z = u.getXCoord()*v.getYCoord() - u.getYCoord()*v.getXCoord();

        return [x,y,z];
    }
}

/**
 * An extension to the array methods. This adds this function
 * which pushes the vertex coordinates individually on an array
 * which is way more handy when it comes down to working with the SingleVertex
 * class and the array structure needed for actually rendering the object
 * @param {SingleVertex} vertex 
 */
Array.prototype.pushSingleVertex = function(vertex) {
    let pos = vertex.getCoordsAsArray();
    this.push(pos[0], pos[1], pos[2]);
}
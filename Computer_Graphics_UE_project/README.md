# CG Lab Project

Submission template for the CG lab project at the Johannes Kepler University Linz.

## Explanation

This `README.md` needs to be pushed to Github for each of the 3 delivery dates.
For every submission change/extend the corresponding sections by replacing the *TODO* markers. Make sure that you push everything to your Github repository before the respective deadlines. For more details, see the Moodle page.

## Concept submission due on 26.03.2021

### Movie Name

Computer Street Parade - The Firewall Riots

### Group Members

|               | Student ID    | First Name  | Last Name      | E-Mail         |
|---------------|---------------|-------------|----------------|----------------|
| **Student 1** | k11812499          | Simon        | Sternbauer           | k11812499@students.jku.at           |
| **Student 2** | k11826767          | Eva          | Mayer                | k11826767@students.jku.at           |

### Concept Submission due on 26.03.2021

Corona has, most obviously, impacted our daily life in many different ways. One often overlooked aspect is the impact this pandemic has on minority groups which rely on public visability to have their needs heared. One of the affected movements is the LGBTQ+ community which cannot hold the annual pride parades in cities all over the world. Therefore we decided to take the parade to the (non-binary) bit lanes of our computers.

We plan do place the movie on a bridge with houses on both sides. People taking part in the parade will move from one end to the other. One or more will hold a torch which uses the particle effect for the flames. The camera will start at the one shore side where the parade starts as well. The cam will zoom out into a bird perspective over the middle of the bridge which will demonstrate the Level of Detail effect, then changes the view to the other end of the bridge where the parades comes to a hold.

We will use the following objects:

* Bridge over the water
* water under the bridge
* houses on both ends of the bridge, one of them will resemble the Ars Eletronica Center
* Next to the houses, the area will fade out in a park-like green space with some trees.
* Simple figures which represent humans, where one or more will hold a torch that incooperates the fire/flame effect
* For static light sources, some street lanterns will be placed along the prides path

### Special Effects

Selected special effects must add up to exactly 30 points. Replace yes/no with either yes or no.

| Selected   | ID | Name                                  | Points |
|------------|----|---------------------------------------|--------|
| no     | S1 | Multi texturing                       | 10     |  
| yes    | S2 | Level of detail                       | 10     |
| no     | S3 | Billboarding                          | 10     |
| no     | S4 | Terrain from heightmap                | 20     |
| no     | S5 | Postprocessing shader                 | 20     |
| no     | S6 | Animated water surface                | 20     |
| no     | S7 | Minimap                               | 20     |
| yes    | S8 | Particle system (rain, smoke, fire)   | 20     |
| no     | S9 | Motion blur                           | 30     |
| no     | SO | Own suggestion (preapproved by email) | --     |

## Intermediate Submission due on 23.04.2021

Prepare a first version of your movie that:

* is 30 seconds long,
* contains animated objects, and
* has an animated camera movement.

Push your code on the day of the submission deadline.

The repository needs to contain:

* code/ Intermediate code + resources + libs
* video/ A screen recording of the intermediate result

Nothing to change here in `README` file.

**Note:** You don’t need to use any lighting, materials, or textures yet. This will be discussed in later labs and can be added to the project afterwards!

## Final Submission due on 22.06.2021

The repository needs to contain:

* code/ Documented code + resources + libs
* video/ A screen recording of the movie
* README.md

### Workload

| Student ID     | Workload (in %) |
| ---------------|-----------------|
| k11812499           | 50            |
| k11826767           | 50            |

Workload has to sum up to 100%.

### Effects

Select which effects you have implemented in the table below. Replace yes/no/partial with one of the options.
Mention in the comments column of the table where you have implemented the code and where it is visible (e.g., spotlight is the lamp post shining on the street).

| Implemented    | ID | Name                                                                                                   | Max. Points | Issues/Comments |
|----------------|----|--------------------------------------------------------------------------------------------------------|-------------|-----------------|
| yes | 1a | Add at least one manually composed object that consists of multiple scene graph nodes.                 | 6           | Car with seperate tires, windows, front lights and main frame |
| yes | 1b | Animate separate parts of the composed object and also move the composed object itself in the scene.   | 4           | Car frame as the main object moves as a whole, tires do spin |
| yes | 1c | Use at least two clearly different materials for the composed object.                                  | 3           | Windows of car, tires, car frame and lights clearly different |
| yes | 2a | Create one scene graph node that renders a complex 3D shape. Fully specify properties for this object. | 7           | Fish in the water |
| yes | 2b | Apply a texture to your self-created complex object by setting proper texture coordinates.             | 3           |                 |
| yes | 3a | Use multiple light sources.                                                                            | 5           | One main global light source + multiple spotlights |
| yes | 3b | One light source should be moving in the scene.                                                        | 3           | Actually two moving light sources attached to the car |
| yes | 3c | Implement at least one spot-light.                                                                     | 8           | Twelve spotlights on the bridge |
| yes | 3d | Apply Phong shading to all objects in the scene.                                                       | 4           |                 |
| yes | 4  | The camera is animated 30 seconds without user intervention. Animation quality and complexity of the camera and the objects influence the judgement.                                                                       | 7           | Using a self-implemented camera system, see also notes below |
| yes | S2 | Level of detail                                                                         | 10        |                 |
| yes | S8 | Particle system (fire)                                                                             | 20        |                 |
| yes | SE | Special effects are nicely integrated and well documented                                              | 20          |                 |

### Self-implemented camera system

We decided on using our own camera system implementation based on [learnopengl.com](https://learnopengl.com/Getting-started/Camera) instead of the one provided, because it seemed more natural to us. The movement uses the same keys (WASD and QE), but it also adds the feature of a pointer lock which enables the user to look around in the scene like in a video game without the need to left click. The lock is enabled/disabled by hitting the `spacebar` once.

## Special Effect Description

### Level of detail

Depending on the distance of the objects coordinates to the camera, using the squared distance meassurement, a different model with a higher or level of detail for the strucure is used to render the object in the scene. The  distances are set such that all three stages of the details are visible (sometimes multiple times) throughout the animated parts.

For the level od detail effect, an own node, which extends the RenderSGNode, is implemented. This class holds three different models with different level of details (low, medium, high). When the render method is called, it is decided, as already mentioned based on the distance what the correct model is and sets it as the render.

The effect is applied to the buildings (two on each side of the river) as they are clearly visible, the changes are also easy well noticeable during the camera movement and due to their size they also seemed to be the objects in the scene which could provide the most benefit to render performance. Also generating three different models for each item in the scene, such as the trees, the bridge, the clouds and our composed car would be very time intensive and would have drained precious time from other important areas of the project.

### Particle system

For this particle effect, we decided to use it for a fire effect that is used on some torches which are placed alongside the moving crowd. The different particles in our particle system are points in the scene that follow a cone-shaped path to a converging point which the particles want to reach. Other possibilities instead of the point would be lines or even textures, which we chose not to use, but go with the points as they fullfill their jobs rather nicely.

When creatin a particle node, various data is initialized as well, one of the is the lifetime of the particle (until it is not visible anymore using the alpha value) and a random seed. The seed is important to make the particles look more organic and give them more volume, it also prevents a "solid" cutoff line where every particle has no alpha value anymore. The third data generated is a direction, which is generated via the passed direction function in the construction. This one is important to get the correct position of the single particle in the shader via multiplication with the current position along the line the particle takes in the cone shaped volume.

For each render cycle, therefore when the render function is called, first new particles are spawned and the lifetime of all particles is decreased. The now locally updated values are move into the buffer on the GPU. Next on various uniform parameters are set and the alpha channel is activated as well as the direction, seeds and lifetime being written into the buffers as well. The needed calculations in the shader are not that complicated. In the vertex shader, the position of the vertice is calculated, transformation and projection are applied. In the fragment shader the color is set and the alpha value, based on the lifetime of the particle, is set somewhere between 0 and 1. This means that older particles cannot be seen as well or are completly faded out if they are not renewed in the next render cycle.

//the OpenGL context
var gl = null;

// scenegraph root node
var root = null;

//Canvas where to request a mouselock onto
var canvas;
let time = 0;

/**
 * initializes OpenGL context, compile shader, and load buffers
 */
function init(resources) {
  //create a GL context with width and height
  gl = createContext(1024, 1024);

  //compile and link shader program
  program = createProgram(gl, resources.vs, resources.fs);
  simple_program = createProgram(gl, resources.vs_single, resources.fs_single);
  particle_program = createProgram(gl, resources.vs_particle, resources.fs_particle);
  root = createSceneGraph(gl, resources);

  canvas = gl.canvas;
  camera.addListeners();
}


function createSceneGraph(gl, resources) {
  //create scenegraph
  const root = new ShaderSGNode(program);

  // create white light node
  light = new LightSGNode();
  light.ambient = [.5, .5, .5, 1];
  light.diffuse = [1, 1, 1, 1];
  light.specular = [0.4, 0.4, 0.4, 0];
  light.position = [0, 27, 60];
  root.append(light); //Add light to the scene

  {
    initLandscape(resources);   //Init the landscape
    initLandscapeLighting();    //Init the spotlights
    initFish(resources); //Create the small fish in the river

    root.append(landscape.lights);  //Append the lights on the bridge using Spotlights
    root.append(ReferenceNode = ComposedCar.create(resources)); //Init and create the reference node
    root.append(landscape.nodes);   //Append the landscape to the scene
    root.append(fish.nodes); //Append fish to the scene

    CrowdFunc.initCrowd(resources, root); //Initialize the crowd
    CrowdFunc.initTorchHolders(resources, root); //Init torch holders
  }

  return root;
}

/**
 * render one frame
 */
function render(timeInMilliseconds) {
  time = timeInMilliseconds;
  // check for resize of browser window and adjust canvas sizes
  checkForWindowResize(gl);

  gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight);
  gl.clearColor(0.9, 0.9, 1.0, 1.0);
  //clear the buffer
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
  //enable depth test to let objects in front occluse objects further away
  gl.enable(gl.DEPTH_TEST);

  //Create projection Matrix and context for rendering.
  const context = createSGContext(gl);
  context.projectionMatrix = mat4.perspective(mat4.create(), glm.deg2rad(30), gl.drawingBufferWidth / gl.drawingBufferHeight, 0.01, 250);

  camera.update();
  context.viewMatrix = camera.view;

  //Render scene
  root.render(context);

  //request another call as soon as possible
  requestAnimationFrame(render);
}

//load the shader resources using a utlity function
loadResources({
  vs: './src/shader/phong.vs.glsl',
  fs: './src/shader/phong.fs.glsl',
  vs_single: './src/shader/single.vs.glsl',
  fs_single: './src/shader/single.fs.glsl',
  vs_particle: './src/shader/particles.vs.glsl',
  fs_particle: './src/shader/particles.fs.glsl',
  crowdMember: './src/models/people.obj',
  torch: './src/models/torch.obj',
  car: './src/models/car.obj',
  car_windows: './src/models/car_windows.obj',
  car_lights: './src/models/car_lights.obj',
  car_mirrors: './src/models/car_mirrors.obj',
  car_bumpers: './src/models/car_bumpers.obj',
  tiresAxis: './src/models/tiresAxis.obj',
  cloud: './src/models/cloud.obj',
  bridge: './src/models/bridge.obj',
  tree: './src/models/tree.obj',
  c3po: './src/models/C-3PO.obj',
  building_high: './src/models/building_high.obj',
  building_medium: './src/models/building_medium.obj',
  building_low: './src/models/building_low.obj',
  water_texture: './src/textures/water.jpg',
  wall_texture: './src/textures/wall.jpg',
  gras_texture: './src/textures/gras.jpg',
  asphalt_texture: './src/textures/asphalt.jpg',
  fish_texture: './src/textures/clownfish.jpg'
}).then(function (resources /*an object containing our keys with the loaded resources*/) {
  init(resources);
  requestAnimationFrame(render);
});
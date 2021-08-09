/**
 * the OpenGL context
 * @type {WebGLRenderingContext}
 */
var gl = null;

/**
 * initializes OpenGL context, compile shader, and load buffers
 */
function init(resources) {
  //create a GL context
  gl = createContext(800 /*width*/, 450 /*height*/);
  
  //TODO initialize shader program and buffers...
  //in WebGL/OpenGL3 we have to create and use our own shaders for the programmable pipeline
  //create the shader program
  shaderProgram = createProgram(gl, resources.vs, resources.fs);

  //create a buffer and put a single clipspace rectangle in it (2 triangles)
  buffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
  //we need typed arrays
  const arr = new Float32Array([
    -1.0, 1.0,
    -1.0, 0.67,
    1.0, 1.0,
    -1.0, 0.67,
    1.0, 1.0,
    1.0, 0.67,
    -1.0, 0.67,
    -1.0, 0.33,
    1.0, 0.67,
    -1.0, 0.33,
    1.0, 0.67,
    1.0, 0.33,
    -1.0, 0.33,
    -1.0, 0.0,
    1.0, 0.33,
    -1.0, 0.0,
    1.0, 0.33,
    1.0, 0.0,
    -1.0, 0.0,
    -1.0, -0.33,
    1.0, 0.0,
    -1.0, -0.33,
    1.0, 0.0,
    1.0, -0.33,
    -1.0, -0.33,
    -1.0, -0.67,
    1.0, -0.33,
    -1.0, -0.67,
    1.0, -0.33,
    1.0, -0.67,
    -1.0, -0.67,
    -1.0, -1.0,
    1.0, -0.67,
    -1.0, -1.0,
    1.0, -0.67,
    1.0, -1.0
  ]);
  //copy data to GPU
  gl.bufferData(gl.ARRAY_BUFFER, arr, gl.STATIC_DRAW);

  colorBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
  const colors = new Float32Array([
    1, 0, 0.09, 1,
    1, 0, 0.09, 1,
    1, 0, 0.09, 1,
    1, 0, 0.09, 1,
    1, 0, 0.09, 1,
    1, 0, 0.09, 1,
    1, 0.64, 0.17, 1,
    1, 0.64, 0.17, 1,
    1, 0.64, 0.17, 1,
    1, 0.64, 0.17, 1,
    1, 0.64, 0.17, 1,
    1, 0.64, 0.17, 1,
    1, 1, 0, 1,
    1, 1, 0, 1,
    1, 1, 0, 1,
    1, 1, 0, 1,
    1, 1, 0, 1,
    1, 1, 0, 1,
    0, 0.5, 0.09, 1,
    0, 0.5, 0.09, 1,
    0, 0.5, 0.09, 1,
    0, 0.5, 0.09, 1,
    0, 0.5, 0.09, 1,
    0, 0.5, 0.09, 1,
    0, 0, 0.97, 1,
    0, 0, 0.97, 1,
    0, 0, 0.97, 1,
    0, 0, 0.97, 1,
    0, 0, 0.97, 1,
    0, 0, 0.97, 1,
    0.52, 0, 0.49, 1,
    0.52, 0, 0.49, 1,
    0.52, 0, 0.49, 1,
    0.52, 0, 0.49, 1,
    0.52, 0, 0.49, 1,
    0.52, 0, 0.49, 1
  ]);
  gl.bufferData(gl.ARRAY_BUFFER, colors, gl.STATIC_DRAW);
}

/**
 * render one frame
 */
function render() {
  //specify the clear color
  gl.clearColor(0.9, 0.9, 0.9, 1.0);
  //clear the buffer
  gl.clear(gl.COLOR_BUFFER_BIT);

  //TODO render scene
  //activate shader program that you have initialized before
  gl.useProgram(shaderProgram);

  var userColor = {r: 0.7, g: 0.3, b: 0.4};
  gl.uniform3f(gl.getUniformLocation(shaderProgram, 'u_usercolor'), userColor.r, userColor.g, userColor.b);

  //we look up the internal location after compilation of the shader program given the name of the attribute
  const positionLocation = gl.getAttribLocation(shaderProgram, 'a_position');

  //enable this vertex attribute
  gl.enableVertexAttribArray(positionLocation);
  //use the currently bound buffer for this location
  //each element is a FLOAT with 2 components
  //2 .. number of components
  //float ... type
  //false ... the array should not be normalized
  //stride / offset ... in case you are interleaving different attribute
  gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
  gl.vertexAttribPointer(positionLocation, 2, gl.FLOAT, false, 0, 0);

  const colorLocation = gl.getAttribLocation(shaderProgram, 'a_color');
  gl.enableVertexAttribArray(colorLocation);
  gl.bindBuffer(gl.ARRAY_BUFFER, colorBuffer);
  gl.vertexAttribPointer(colorLocation, 4, gl.FLOAT, false, 0, 0);

  // draw the bound data as 6 vertices = 2 triangles starting at index 0
  gl.drawArrays(gl.TRIANGLES, 0, 36);

  //request another call as soon as possible
  //requestAnimationFrame(render)

}

loadResources({
  //list of all resources that should be loaded as key: path
  vs: 'shader/simple.vs.glsl',
  fs: 'shader/simple.fs.glsl'
}).then(function (resources /*loaded resources*/) {
  init(resources);
  //render one frame
  render();
});

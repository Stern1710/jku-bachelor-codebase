/**
 * as simple fragment shader just setting the provided color as fragment color
 * Created by Samuel Gratzl on 08.02.2016.
 */

//need to specify how "precise" float should be
precision mediump float;

uniform vec3 u_usercolor;

//interpolate argument between vertex and fragment shader
varying vec4 v_color;

//entry point again
void main() {
  //gl_FragColor ... magic output variable containing
  //                 the final 4D color of the fragment
  // gl_FragColor = vec4(u_usercolor, 1);

  gl_FragColor = v_color;
}

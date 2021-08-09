/**
 * a phong shader implementation
 * Created by Samuel Gratzl on 29.02.2016.
 * Extended with spotlight and other light sources in the May 2021
 * by Sternbauer and Eva Mayer
 */
attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord;

uniform mat4 u_modelView;
uniform mat3 u_normalMatrix;
uniform mat4 u_projection;

uniform vec3 u_lightPos;
uniform vec3 u_spotlight0Pos;
uniform vec3 u_spotlight1Pos;
uniform vec3 u_spotlight2Pos;
uniform vec3 u_spotlight3Pos;
uniform vec3 u_spotlight4Pos;
uniform vec3 u_spotlight5Pos;
uniform vec3 u_spotlight6Pos;
uniform vec3 u_spotlight7Pos;
uniform vec3 u_spotlight8Pos;
uniform vec3 u_spotlight9Pos;
uniform vec3 u_spotlight10Pos;
uniform vec3 u_spotlight11Pos;

uniform vec3 u_spotlightMov1Pos;
uniform vec3 u_spotlightMov2Pos;

//output of this shader
varying vec3 v_normalVec;
varying vec3 v_eyeVec;
varying vec3 v_lightVec;
varying vec3 v_light2Vec;

varying vec3 v_spotlight0;
varying vec3 v_spotlight1;
varying vec3 v_spotlight2;
varying vec3 v_spotlight3;
varying vec3 v_spotlight4;
varying vec3 v_spotlight5;
varying vec3 v_spotlight6;
varying vec3 v_spotlight7;
varying vec3 v_spotlight8;
varying vec3 v_spotlight9;
varying vec3 v_spotlight10;
varying vec3 v_spotlight11;

varying vec3 v_spotlightMov1;
varying vec3 v_spotlightMov2;

varying vec2 v_texCoord;

void main() {
	vec4 eyePosition = u_modelView * vec4(a_position,1);

  	v_normalVec = u_normalMatrix * a_normal;

  	v_eyeVec = -eyePosition.xyz;
	//light position as uniform
	v_lightVec = u_lightPos - eyePosition.xyz;
	//Position of all the spotlights (after the view transform)
	v_spotlight0 = u_spotlight0Pos - eyePosition.xyz;
	v_spotlight1 = u_spotlight1Pos - eyePosition.xyz;
	v_spotlight2 = u_spotlight2Pos - eyePosition.xyz;
	v_spotlight3 = u_spotlight3Pos - eyePosition.xyz;
	v_spotlight4 = u_spotlight4Pos - eyePosition.xyz;
	v_spotlight5 = u_spotlight5Pos - eyePosition.xyz;
	v_spotlight6 = u_spotlight6Pos - eyePosition.xyz;
	v_spotlight7 = u_spotlight7Pos - eyePosition.xyz;
	v_spotlight8 = u_spotlight8Pos - eyePosition.xyz;
	v_spotlight9 = u_spotlight9Pos - eyePosition.xyz;
	v_spotlight10 = u_spotlight10Pos - eyePosition.xyz;
	v_spotlight11 = u_spotlight11Pos - eyePosition.xyz;

	v_spotlightMov1 = u_spotlightMov1Pos - eyePosition.xyz;
	v_spotlightMov2 = u_spotlightMov2Pos - eyePosition.xyz;

	//Just pass the texture coordinate to the fragment shader
	v_texCoord = a_texCoord;

	gl_Position = u_projection * eyePosition;
}

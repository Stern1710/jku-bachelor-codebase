/**
 * a phong shader implementation
 * Created by Samuel Gratzl on 29.02.2016.
 * Extended with spotlight and other light sources in the May 2021
 * by Sternbauer and Eva Mayer
 */
precision mediump float;

/**
 * definition of a material structure containing common properties
 */
struct Material {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec4 emission;
	float shininess;
};

/**
 * definition of the light properties related to material properties.
 */
struct Light {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
};

/*
* Very similiab to the normal light, but has a direction and a cutoff angle.
* This means that outside of this angle, the spotlight has no effect on the lighting
*/
struct Spotlight {
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec3 direction;
	float cutoffAngle;
};

uniform Material u_material;
uniform Light u_light;

// For texturing
uniform bool u_enableObjectTexture;
varying vec2 v_texCoord;
uniform sampler2D u_tex;

// All spotlight uniforms
uniform Spotlight u_spotlight0;
uniform Spotlight u_spotlight1;
uniform Spotlight u_spotlight2;
uniform Spotlight u_spotlight3;
uniform Spotlight u_spotlight4;
uniform Spotlight u_spotlight5;
uniform Spotlight u_spotlight6;
uniform Spotlight u_spotlight7;
uniform Spotlight u_spotlight8;
uniform Spotlight u_spotlight9;
uniform Spotlight u_spotlight10;
uniform Spotlight u_spotlight11;

uniform Spotlight u_spotlightMov1;
uniform Spotlight u_spotlightMov2;

// All spotlight vectors
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

//varying vectors for light computation
varying vec3 v_normalVec;
varying vec3 v_eyeVec;
varying vec3 v_lightVec;


vec4 calculateSimplePointLight(Light light, Material material, vec3 lightVec, vec3 normalVec, vec3 eyeVec, vec4 textureColor) {
	// You can find all built-in functions (min, max, clamp, reflect, normalize, etc.) 
	// and variables (gl_FragCoord, gl_Position) in the OpenGL Shading Language Specification: 
	// https://www.khronos.org/registry/OpenGL/specs/gl/GLSLangSpec.4.60.html#built-in-functions
	lightVec = normalize(lightVec);
	normalVec = normalize(normalVec);
	eyeVec = normalize(eyeVec);

	//If the texture is enabled, multiply the textureColor on ambient and diffuse
	//We chose multiplication instead of overwriting as we felt it made the scene look better
	if (u_enableObjectTexture) { 
        material.diffuse *= textureColor;
        material.ambient *= textureColor;
    }

  	// implement phong shader
	//compute diffuse term
	float diffuse = max(dot(normalVec,lightVec),0.0);

	//compute the different terms and calculate the final color and return
	vec3 reflectVec = reflect(-lightVec,normalVec);
	float spec = pow( max( dot(reflectVec, eyeVec), 0.0) , material.shininess);

	vec4 c_amb  = clamp(light.ambient * material.ambient, 0.0, 1.0);
	vec4 c_diff = clamp(diffuse * light.diffuse * material.diffuse, 0.0, 1.0);
	vec4 c_spec = clamp(spec * light.specular * material.specular, 0.0, 1.0);
	vec4 c_em   = material.emission;

	return c_amb + c_diff + c_spec + c_em;
}

/*
* The spotlight works very much like the normal light, but with a small, yet important difference
* 
*/
vec4 calculateSpotlight(Spotlight light, Material material, vec3 lightVec, vec3 normalVec, vec3 eyeVec, vec4 textureColor) {
	lightVec = normalize(lightVec);
	normalVec = normalize(normalVec);
	eyeVec = normalize(eyeVec);

    if (u_enableObjectTexture) {
        material.diffuse *= textureColor;
        material.ambient *= textureColor;
    }

  	// implement phong shader
	//compute diffuse term
	float diffuse = max(dot(normalVec,lightVec),0.0);

	//compute specular term
	vec3 reflectVec = reflect(-lightVec,normalVec);
	float spec = pow( max( dot(reflectVec, eyeVec), 0.0) , material.shininess);


	vec4 c_amb  = clamp(light.ambient * material.ambient, 0.0, 1.0);
	vec4 c_diff = clamp(diffuse * light.diffuse * material.diffuse, 0.0, 1.0);
	vec4 c_spec = clamp(spec * light.specular * material.specular, 0.0, 1.0);
	vec4 c_em   = material.emission;

	//Calcualte the dot product between the light and the negative light vector
	//If the angle is smaller that the passed cutoff, the it is within the spotlight
	//and the emissed light if the spotlight is returned
	//Otherwise return the "normal" calculated term
	float angle = dot(normalize(light.direction), -lightVec);
	if (angle <= light.cutoffAngle) {
		return c_amb + c_em;
	}
	return c_amb + c_diff + c_spec + c_em;
}

void main() {
	vec4 textureColor = vec4(0,0,0,1);
	if (u_enableObjectTexture) {
		textureColor = texture2D(u_tex,v_texCoord);
	}
	gl_FragColor = 
		//Add together the global light point
		calculateSimplePointLight(u_light, u_material, v_lightVec, v_normalVec, v_eyeVec, textureColor)
		//All of our spotlights for the laterns
		+ 0.55 * calculateSpotlight(u_spotlight0, u_material, v_spotlight0, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight1, u_material, v_spotlight1, v_normalVec, v_eyeVec, textureColor)
		+ 0.30 * calculateSpotlight(u_spotlight2, u_material, v_spotlight2, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight3, u_material, v_spotlight3, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight4, u_material, v_spotlight4, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight5, u_material, v_spotlight5, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight6, u_material, v_spotlight6, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight7, u_material, v_spotlight7, v_normalVec, v_eyeVec, textureColor)
		+ 0.30 * calculateSpotlight(u_spotlight8, u_material, v_spotlight8, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight9, u_material, v_spotlight9, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight10, u_material, v_spotlight10, v_normalVec, v_eyeVec, textureColor)
		+ 0.55 * calculateSpotlight(u_spotlight11, u_material, v_spotlight11, v_normalVec, v_eyeVec, textureColor)
		//The moving spotlights which travel with the car
		+ calculateSpotlight(u_spotlightMov1, u_material, v_spotlightMov1, v_normalVec, v_eyeVec, textureColor)
		+ calculateSpotlight(u_spotlightMov2, u_material, v_spotlightMov2, v_normalVec, v_eyeVec, textureColor);
}

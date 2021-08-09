uniform mat4 u_modelView;
uniform mat4 u_projection;

attribute vec3 a_direction;
attribute float a_lifespan;
attribute float a_seed;

varying float v_lifespan;

void main() {
    v_lifespan = a_lifespan;
    gl_PointSize = 3.0; //Size of particle

    //The position of the particle in the world
    float len = a_lifespan + a_seed;
    vec4 pos = vec4(
        a_direction * len,
        1.0
    );
    //Transformation in the scene to place the particle correctly
    vec4 eyePosition = u_modelView * pos;
    gl_Position = u_projection * eyePosition;
}

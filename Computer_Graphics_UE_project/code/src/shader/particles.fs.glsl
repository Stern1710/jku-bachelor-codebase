precision mediump float;

varying float v_lifespan;

void main() {
    gl_FragColor = vec4( //Mostly red and a bit of green for orange shade  
        1.0,  
        0.3,
        0.0,
        clamp(v_lifespan - 0.05,
            0.0, //Lower bound
            1.0  //Upper bound
        ) //Clamp makes lifespan (alpha value) to lay between 0.0 and 1.0
    );
}
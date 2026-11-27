#version 330
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D InSampler;
uniform sampler2D DepthSampler;

layout(std140) uniform SamplerInfo {
    vec2 OutSize;
};

layout(std140) uniform PlanetConfig {
    vec4 BodyA;
    vec4 BodyB;
    vec4 Tint;
};

layout(location = 0) in vec2 texCoord;
layout(location = 0) out vec4 fragColor;

float continentField(vec3 p) {
    float a = sin(p.x * 8.3 + sin(p.z * 5.1) * 1.7);
    float b = cos(p.y * 9.2 + sin(p.x * 3.4) * 1.4);
    float c = sin((p.x + p.y - p.z) * 6.7);
    return a * b * 0.58 + c * 0.42;
}

vec4 renderBody(vec2 uv, vec4 body) {
    if (body.z <= 0.0) return vec4(0.0);

    vec2 point = (uv - body.xy) * vec2(OutSize.x / OutSize.y, 1.0) / body.z;
    float radiusSq = dot(point, point);
    if (radiusSq > 1.0) return vec4(0.0);

    float sphereZ = sqrt(max(0.0, 1.0 - radiusSq));
    vec3 normal = normalize(vec3(point.x, -point.y, sphereZ));
    vec3 pixelNormal = floor(normal * 96.0) / 96.0;
    float landShape = continentField(pixelNormal);
    vec3 color;

    if (body.w < 0.5) {
        vec3 ocean = vec3(0.025, 0.12, 0.34);
        vec3 land = vec3(0.22, 0.43, 0.19);
        vec3 highland = vec3(0.58, 0.49, 0.31);
        float landMask = smoothstep(-0.08, 0.12, landShape);
        color = mix(ocean, land, landMask);
        color = mix(color, highland, smoothstep(0.48, 0.68, landShape) * 0.58);
        float cloudPattern = sin(pixelNormal.x * 23.0 + pixelNormal.z * 11.0)
                           * cos(pixelNormal.y * 27.0 + pixelNormal.x * 7.0);
        float clouds = smoothstep(0.54, 0.78, cloudPattern) * 0.36;
        color = mix(color, vec3(0.82, 0.88, 0.94), clouds);
    } else if (body.w < 1.5) {
        float ridges = sin(pixelNormal.x * 19.0 + sin(pixelNormal.z * 8.0))
                     * cos(pixelNormal.y * 16.0 + pixelNormal.z * 12.0);
        float darkRock = smoothstep(-0.52, 0.35, ridges);
        color = mix(vec3(0.20, 0.055, 0.035), vec3(0.72, 0.22, 0.095), darkRock);
        float dust = smoothstep(0.52, 0.82, landShape) * 0.24;
        color = mix(color, vec3(0.92, 0.47, 0.22), dust);
    } else {
        float craters = sin(pixelNormal.x * 31.0) * sin(pixelNormal.y * 27.0)
                      * sin(pixelNormal.z * 25.0);
        color = mix(vec3(0.16, 0.15, 0.14), vec3(0.39, 0.34, 0.29), smoothstep(-0.2, 0.55, craters));
    }

    vec3 lightDirection = normalize(vec3(-0.42, 0.28, 0.86));
    float lighting = max(dot(normal, lightDirection), 0.0);
    color *= 0.22 + lighting * 0.78;
    float limb = 1.0 - smoothstep(0.78, 1.0, radiusSq);
    color = mix(color, vec3(0.30, 0.62, 0.98), limb * 0.22);
    return vec4(color * Tint.rgb, 1.0);
}

void main() {
    vec4 scene = texture(InSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;

    // 26.3 uses reversed depth: the far plane / sky is zero.
    if (depth < 0.0005) {
        vec4 bodyA = renderBody(texCoord, BodyA);
        vec4 bodyB = renderBody(texCoord, BodyB);
        if (bodyA.a > 0.0) scene = mix(scene, bodyA, bodyA.a);
        if (bodyB.a > 0.0) scene = mix(scene, bodyB, bodyB.a);
    }
    fragColor = scene;
}

/*
Copyright (C) 2018 UnityCindy3D Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

Shader "GeometricObject/DoubleSided"
{
	Properties
	{
		_FColor ("Front Color", Color) = (1,1,1,1)
		_BColor ("Back Color",  Color) = (1,1,1,1)
		_Shininess ("Shininess", Range(0.01, 1) ) = 0.5
	}

    SubShader {

        Tags { "Queue"="Transparent" "RenderType"="Transparent" }

        LOD 100

        // render to depth buffer
        Pass {
          Cull Off
          ZWrite On
          ColorMask 0
        }

        // render back-face
        Cull Front

        CGPROGRAM

        #pragma surface surf BlinnPhong alpha

        struct Input {
            float4 color : COLOR;
        };

        fixed4 _BColor;
        half _Shininess;

        void surf (Input IN, inout SurfaceOutput o) {
            fixed4 c = _BColor;
            o.Albedo = c.rgb;
            o.Alpha = c.a;
            o.Specular = _Shininess;
			o.Gloss = 1;
        }

        ENDCG

        // render front-face
        Cull Back

        CGPROGRAM

        #pragma surface surf BlinnPhong alpha

        struct Input {
            float4 color : COLOR;
        };

        fixed4 _FColor;
        half _Shininess;

        void surf (Input IN, inout SurfaceOutput o) {
            fixed4 c = _FColor;
            o.Albedo = c.rgb;
            o.Alpha = c.a;
            o.Specular = _Shininess;
			o.Gloss = 1;
        }

        ENDCG
	}

	FallBack "Diffuse"
}
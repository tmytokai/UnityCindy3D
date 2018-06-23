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

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class StandAloneSample : MonoBehaviour {

	public GameObject GeometricObjectPrefab;

	private GameObject test01 = null;
	private GameObject test02 = null;

	void Start () {		
	}

	void Update () {

		if (Input.GetKeyDown (KeyCode.F1)) {

			if (test01 == null) {
				test01 = Instantiate (GeometricObjectPrefab, new Vector3( 0f,0f,0f), Quaternion.identity );
				test01.name = "TorusKnot";
				TorusKnot (test01.GetComponent<GeometricObject> ());
			} else {
				Destroy (test01);
				test01 = null;
			}
		}

		if (Input.GetKeyDown (KeyCode.F2)) {

			if (test02 == null) {
				test02 = Instantiate (GeometricObjectPrefab, new Vector3( 0f,0f,0f), Quaternion.identity );
				test02.name = "Enneper";
				Enneper (test02.GetComponent<GeometricObject> ());
			} else {
				Destroy (test02);
				test02 = null;
			}
		}
	}

	// see https://en.wikipedia.org/wiki/Torus_knot
	void TorusKnot ( GeometricObject obj ){

		System.Func<float, Vector3> f = (phi) => {
			var o = new Vector3 ();
			var p = 3f;
			var q = 8f;
			var r = Mathf.Cos (q * phi) + 2f;
			o.x = r * Mathf.Sin (p * phi);
			o.y = r * Mathf.Cos (p * phi);
			o.z = -Mathf.Sin (q * phi);
			return o*0.5f;
		};

		var n = 500;
		var frontColors = new List<Color32> ();
		var points = new List<Vector3> ();

		frontColors.Clear ();
		for (var i = 0; i < n; ++i) {
			var	hue = (float)i / n;
			frontColors.Add( Color.HSVToRGB( hue,1f,1f ) );
		}

		points.Clear();
		for (var i = 0; i < n; ++i) {
			var w = 2f * Mathf.PI * (float)i / n;
			points.Add ( f(w) );
		}

		obj.Begin ();
		obj.SetLineRadius (0.15f);
		obj.SetLineTopology (Pen.Topology.Close);
		obj.AddLine( points, frontColors );
		obj.End ();
	}

	// see https://en.wikipedia.org/wiki/Enneper_surface
	void Enneper ( GeometricObject obj ){

		System.Func<int, int, Vector3> f = (r, c) => {
			var o = new Vector3 ();
			var u = (float)r / 10f;
			var v = (float)c / 10f;
			o.x = u * (1f - u * u + v * v) / 3f; 
			o.y = v * (1f - v * v + u * u) / 3f;
			o.z = (u * u - v * v) / 3f;
			return o*0.5f;
		};

		var rows = 51;
		var cols = 51;
		var idxs = new int[rows, cols];
		var points = new List<Vector3> ();

		points.Clear();
		for ( var r = -rows/2; r <= rows/2; r++) {			
			for (var c = -cols/2; c <= cols/2; c++) {
				idxs [rows/2+r, cols/2+c] = points.Count;
				points.Add (f(r,c));
			}
		}

		obj.Begin ();
		obj.SetPointFrontColor( new Color (1.0f, 1.0f, 1.0f, 1.0f) );
		for (var r = 0; r < rows; r++) {			
			for (var c = 0; c < cols; c++) {
				obj.AddPoint (points [idxs[r,c]]);
			}
		}
		obj.SetMeshFrontColor (new Color (0.5f, 0.5f, 1.0f, 1.0f) );
		obj.SetMeshBackColor ( new Color (0.5f, 0.5f, 0.0f, 1.0f) );
		obj.AddMesh( rows, cols, points, idxs );
		obj.End ();
	}

}

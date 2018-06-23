﻿/*
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

using System;
using System.Threading;

public class Manager : MonoBehaviour {

	public GameObject GeometricObjectPrefab;

	public static SynchronizationContext context{ get; private set; }
	private Server server = null;
	private Decoder decoder = null;

	void Start () {
		context = SynchronizationContext.Current;
		decoder = new Decoder (gameObject.GetComponent<Manager> ());
		server = new Server (decoder);
	}

	void Update () {
	}

	void OnApplicationQuit ()
	{
		server.Quit ();
		decoder.Quit ();
	}

	public GameObject InstantiateGeometricObject( string name ){

		var obj = GameObject.Find( name );
		if (obj == null) {
			obj = Instantiate (GeometricObjectPrefab);
			obj.name = name;
		}
		return obj;
	}

	public static void DebugLog ( object message )
	{
		if (context != null) {

			if (context != SynchronizationContext.Current) {
				var id = System.Threading.Thread.CurrentThread.ManagedThreadId;
				context.Post ( (state) => Debug.Log (state), "[" + id + "] " + message);
			} else {
				Debug.Log ("[UI] " + message);
			}				
		}
	}

	public static void DebugLogWarning(string message)
	{
		if (context != null) {

			if (context != SynchronizationContext.Current) {
				var id = System.Threading.Thread.CurrentThread.ManagedThreadId;
				context.Post ( (state) => Debug.LogWarning (state), "[" + id + "] " + message);
			} else {
				Debug.LogWarning ("[UI] " + message);
			}				
		}
	}

	public static void DebugLogError(string message)
	{
		if (context != null) {

			if (context != SynchronizationContext.Current) {
				var id = System.Threading.Thread.CurrentThread.ManagedThreadId;
				context.Post ( (state) => Debug.LogError (state), "[" + id + "] " + message);
			} else {
				Debug.LogError ("[UI] " + message);
			}				
		}
	}
}

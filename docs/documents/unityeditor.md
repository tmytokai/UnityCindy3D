---
title: How to Cooperate with Unity Editor
layout: default
---
[Home](../)

# **How to cooperate with Unity Editor**

In cooperation with the Unity Editor, UnityCindy3D can make use of Unity's powerful functions like physics engine and collision system.

Here, let's learn how UnityCindy3D can cooperate with the Unity Editor through programming of a simple game.

## Getting Started

1. Download the code of Viewer from the [Downloads page](https://github.com/tmytokai/UnityCindy3D/releases) and extract it.
1. Launch the Unity Editor.
1. Open "UnityCindy3D/Viewer" folder.
1. Double click "Viewer" icon in the Assets panel to open the game scene.

## Making of Player Object

1. Drag "GeometricObject" from the Assets panel and drop it into the Hierarchy panel.
1. Rename it to "Player".
1. Push "Add Component" button in the Inspector panel and select "Physics &gt; Rigidbody".
1. Uncheck "Use Gravity" item in the "Rigidbody" setting.
1. Set "Position" item in the "Transform" setting to (0,0,-3).
1. Create new C# script file in the Assets panel and rename it to "PlayerScript".
1. Open the script file and copy the following code.

    ```
    using System.Collections;
    using System.Collections.Generic;
    using UnityEngine;

    public class PlayerScript : MonoBehaviour {

        private GameObject bullet;
        private GameObject enemy;

        void Start () {
            bullet = GameObject.Find("Bullet");
            enemy = GameObject.Find("Enemy");
            StartCoroutine ("SpawnBullet");
            StartCoroutine ("SpawnEnemy");
        }

        void Update () {        
            var player_speed = 10f;
            gameObject.GetComponent<Rigidbody> ().velocity = new Vector3 (Input.GetAxis ("Horizontal") * player_speed, 0f, 0f);
        }

        private IEnumerator SpawnBullet(){
        
            while (true) {
                if (bullet != null && Input.GetButton ("Fire1")) {
                    var bullet_speed = 20f;
                    var obj = Instantiate (bullet, new Vector3 ( transform.position.x, 0f, transform.position.z ), Quaternion.identity);
                    obj.GetComponent<Rigidbody> ().velocity = transform.forward * bullet_speed;
                    var mesh = obj.GetComponent<MeshFilter> ().mesh;
                    obj.GetComponent<BoxCollider> ().size = mesh.bounds.size;
                    Destroy (obj, 2f);
                }
                yield return new WaitForSeconds(0.1f);
            }
        }        

        private IEnumerator SpawnEnemy(){

            while (true) {
                if (enemy != null) {
                    var obj = Instantiate ( enemy, new Vector3 ( Random.Range(-4f, 4f), 5f, 0f ), Quaternion.identity);
                    obj.GetComponent<Rigidbody> ().useGravity = true;
                    var mesh = obj.GetComponent<MeshFilter> ().mesh;
                    obj.GetComponent<BoxCollider> ().size = mesh.bounds.size;
                    Destroy (obj, 10f);
                }
                yield return new WaitForSeconds(2f);
            }
        }
    }
    ```

1. Save the script file and get back to the Unity Editor.
1. Click "Player" object in the Hierarchy panel and reshow the inspector of "Player".
1. Drag "PlayerScript" from the Assets panel and drop it into the inspector of "Player".

## Making of Bulllet Object

1. Drag "GeometricObject" from the Assets panel and drop it into the Hierarchy panel.
1. Rename it to "Bullet".
1. Push "Add Component" button in the Inspector panel and select "Physics &gt; Rigidbody".
1. Uncheck "Use Gravity" item in the "Rigidbody" setting.
1. Push "Add Component" button in the Inspector panel again and select "Physics &gt; Box Collider".
1. Set "Position" item in the "Transform" setting to (0,0,-50).

## Making of Enemy Object

1. Drag "GeometricObject" from the Assets panel and drop it into the Hierarchy panel.
1. Rename it to "Enemy".
1. Push "Add Component" button in the Inspector panel and select "Physics &gt; Rigidbody".
1. Uncheck "Use Gravity" item in the "Rigidbody" setting.
1. Push "Add Component" button in the Inspector panel again and select "Physics &gt; Box Collider".
1. Set "Position" item in the "Transform" setting to (0,0,-100).

## Setting Up of Cinderella

1. Launch the Cinderella.
1. Open the Script Editor.
1. Open the Initialization slot and copy the following code.

    ```
    use( "UnityCindy3D" );
    ```
1. Open the Draw slot and copy the following code.

    ```
    begin3d("Player");  
    m=apply(-20..20,ii,u=ii/10;   
      apply(-20..20,jj,v=jj/10;   
      x = u * (1-u*u+v*v)/6;
      y = -v * (1-v*v+u*u)/6;
      z=(u*u-v*v)/6; 
      (x,y,z)));   
    m=flatten(m,level->1);
    mesh3d(41,41,m,color->(0.5,0.5,1.0));
    end3d();

    begin3d("Bullet");  
    drawsphere3d([0,0,0], 0.2, color->[1,1,1]);
    end3d();

    begin3d("Enemy");  
    n=600; 
    p=3; 
    q=8; 
    pt=apply(0..n-1,i, w =i/n*2*pi; 
      r=cos(q*w)+2; 
      ( sin(q*w), r * cos(p*w), r * sin(p*w) ) ); 
    cl=apply(0..n,i, hue( i/n ); ); 
    colorconnect3d(pt,cl,size->3,topology->"close");
    end3d();
    ```

## Let's Play

1. Push the Play putton on the Unity Editor.
1. Push the start button on the Script editor of Cinderella.
1. You can move the player with left/right arrow keys.
1. You can shoot the bullets with left-ctrl key.
1. Of course you can change each object's shape freely by editing the above CindyScript.
1. Enjoy the game !
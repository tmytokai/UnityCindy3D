---
title: Tutorial
layout: default
---
[Back](./)

# Cindy3D

[Cindy3D](http://gagern.github.io/Cindy3D/) is a plugin of Cinderella.

This plugin can draw beautiful 3D graphics easily using OpenGL.

## Getting Started

1. Install the Cindy3D.
1. Launch the Cinderella.
1. Open the Script Editor.
1. Open the Initialization slot, and copy the following code.

    ```
    use("Cindy3D"); 
    ```

## How to draw a point

1. Open the Draw slot, and copy the following code.

    ```
    begin3d(); 

    // draws axes
    background3d([0,0,0]); 
    fieldofview3d(pi/2); 
    draw3d([-5,0,0],[5,0,0],color->[1,0,0],size->0.3); //x 
    draw3d([0,-5,0],[0,5,0],color->[0,1,0],size->0.3); //y 
    draw3d([0,0,-5],[0,0,5],color->[0,0,1],size->0.3); //z 
    lookat3d([3,3,3],[0,0,0],[-1,-1,0]); 
    color3d([1,1,1]); 

    // draws a point  
    draw3d([2,0,1]);

    end3d() 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw a segment

1. Open the Draw slot, and copy the following code.

    ```
    begin3d(); 

    // draws axes
    background3d([0,0,0]); 
    fieldofview3d(pi/2); 
    draw3d([-5,0,0],[5,0,0],color->[1,0,0],size->0.3); //x 
    draw3d([0,-5,0],[0,5,0],color->[0,1,0],size->0.3); //y 
    draw3d([0,0,-5],[0,0,5],color->[0,0,1],size->0.3); //z 
    lookat3d([3,3,3],[0,0,0],[-1,-1,0]); 
    color3d([1,1,1]); 

    // draws a segment
    draw3d([0,0,0],[2,0,1]);

    end3d() 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw polygons

1. Open the Draw slot, and copy the following code.

    ```
    begin3d(); 

    // draws axes
    background3d([0,0,0]); 
    fieldofview3d(pi/2); 
    draw3d([-5,0,0],[5,0,0],color->[1,0,0],size->0.3); //x 
    draw3d([0,-5,0],[0,5,0],color->[0,1,0],size->0.3); //y 
    draw3d([0,0,-5],[0,0,5],color->[0,0,1],size->0.3); //z 
    lookat3d([3,3,3],[0,0,0],[-1,-1,0]); 
    color3d([1,1,1]); 

    // draws polygons  
    A=[2,0,0];  
    B=[0,2,0];  
    C=[-2,0,0];  
    D=[0,0,2];  
    fillpoly3d([A,B,D]);  
    fillpoly3d([B,C,D]);  

    end3d() 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw a sphere

1. Open the Draw slot, and copy the following code.
   
    ```
    begin3d(); 

    // draws axes
    background3d([0,0,0]); 
    fieldofview3d(pi/2); 
    draw3d([-5,0,0],[5,0,0],color->[1,0,0],size->0.3); //x 
    draw3d([0,-5,0],[0,5,0],color->[0,1,0],size->0.3); //y 
    draw3d([0,0,-5],[0,0,5],color->[0,0,1],size->0.3); //z 
    lookat3d([3,3,3],[0,0,0],[-1,-1,0]); 
    color3d([1,1,1]); 

    // draws a sphere  
    drawsphere3d([0,0,0],2);  

    end3d() 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw a curved surface 

1. Open the Draw slot, and copy the following code.

    ```
    begin3d(); 

    // draws axes
    background3d([0,0,0]); 
    fieldofview3d(pi/2); 
    draw3d([-5,0,0],[5,0,0],color->[1,0,0],size->0.3); //x 
    draw3d([0,-5,0],[0,5,0],color->[0,1,0],size->0.3); //y 
    draw3d([0,0,-5],[0,0,5],color->[0,0,1],size->0.3); //z 
    lookat3d([3,3,3],[0,0,0],[-1,-1,0]); 
    color3d([1,1,1]); 

    // draws a curved surface (saddle surface)  
    m=apply(-20..20,ii,x=ii/10;  
      apply(-20..20,jj,y=jj/10;  
      z=(x^2-y^2)/2;  
      (x,y,z)));  
    m=flatten(m,level->1);  
    mesh3d(41,41,m);  

    end3d()   
    ```

1. Push the start button on the right-upper side of the editor.

## Example: Torus knot

1. Open the Draw slot, and copy the following code.
    
    ```
    begin3d(); 

    // draws axes
    background3d([0,0,0]); 
    fieldofview3d(pi/2); 
    draw3d([-5,0,0],[5,0,0],color->[1,0,0],size->0.3); //x 
    draw3d([0,-5,0],[0,5,0],color->[0,1,0],size->0.3); //y 
    draw3d([0,0,-5],[0,0,5],color->[0,0,1],size->0.3); //z 
    lookat3d([3,3,3],[0,0,0],[-1,-1,0]); 
    color3d([1,1,1]); 

    // draws a Torus knot (p,q)  
    n=600;  
    p=3;  
    q=8;  
    f(w):=( sin(q*w), (cos(q*w)+2) * cos(p*w), (cos(q*w)+2) * sin(p*w) ); 
    repeat(n,i, 
      w1=i/n*2*pi; 
      w2=(i+1)/n*2*pi; 
      color3d(hue(i/n)); 
      draw3d(f(w1),f(w2),size->3); 
    ); 
  
    end3d() 
    ```

1. Push the start button on the right-upper side of the editor.

---
title: Tutorial
layout: default
---
[Back](./)

# SpaceCindy2KETpic

[SpaceCindy2KETpic](https://sites.google.com/site/ketcindy/english/cindy3d2ketpic) is a tool to draw 3D graphics directly on the main window of Cinderella.

SpaceCindy is not a plugin package but a script file. So the installation is not needed.

## Getting Started

1. Download "SpaceCindy2KETpic.cdy"
1. Open "SpaceCindy2KETpic.cdy"
1. Open the Script Editor.

## How to draw a point

1. Open the Draw slot, and copy the following code.

    ```
    setview3d(0,15,30); 

    // draws axes
    setaxis3d([-5,5],[-5,5],[-5,5]); 
    axisdisp([[Red,Green,Blue]]);  
    defcolor[[0,0,0]];  

    // draws a point 
    drawpt3d([2,0,1],["size=3"]); 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw a segment

1. Open the Draw slot, and copy the following code.

    ```
    setview3d(0,15,30); 

    // draws axes
    setaxis3d([-5,5],[-5,5],[-5,5]); 
    axisdisp([[Red,Green,Blue]]);  
    defcolor[[0,0,0]];  

    // draws a segment  
    segment3d([[0,0,0],[2,0,1]],["size=3"]); 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw polygons

1. Open the Draw slot, and copy the following code.

    ```
    setview3d(0,15,30); 

    // draws axes
    setaxis3d([-5,5],[-5,5],[-5,5]); 
    axisdisp([[Red,Green,Blue]]);  
    defcolor[[0,0,0]];  

    // draws polygons    
    A=[2,0,0];   
    B=[0,2,0]; 
    C=[-2,0,0];   
    D=[0,0,2];   
    plate3d([A,B,D],[[Black,White]]); 
    plate3d([B,C,D],[[Black,White]]); 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw a sphere

1. Open the Draw slot, and copy the following code.

    ```
    setview3d(0,15,30); 

    // draws axes
    setaxis3d([-5,5],[-5,5],[-5,5]); 
    axisdisp([[Red,Green,Blue]]);  
    defcolor[[0,0,0]];  

    // draws a sphere  
    drawsphere([0,0,0],2); 
    ```

1. Push the start button on the right-upper side of the editor.

## How to draw a curved surface 

1. Open the Draw slot, and copy the following code.

    ```
    setview3d(0,15,30); 

    // draws axes
    setaxis3d([-5,5],[-5,5],[-5,5]); 
    axisdisp([[Red,Green,Blue]]);  
    defcolor[[0,0,0]];  

    // draws a curved surface (saddle surface)    
    surface4(["u","v","(u^2-v^2)/2"],[[-2,2],[-2,2]],["Mesh=[41,41]"]); 
    ```

1. Push the start button on the right-upper side of the editor.

## Example: Torus knot

1. Open the Draw slot, and copy the following code.

    ```
    setview3d(0,15,30); 

    // draws axes
    setaxis3d([-5,5],[-5,5],[-5,5]); 
    axisdisp([[Red,Green,Blue]]);  
    defcolor[[0,0,0]];  

    // draws a Torus knot (p,q)    
    n=600;   
    p=3;   
    q=8;   
    f(w):=( sin(q*w), (cos(q*w)+2) * cos(p*w), (cos(q*w)+2) * sin(p*w) );  
    repeat(n,i,  
      w1=i/n*2*pi;  
      w2=(i+1)/n*2*pi;  
      segment3d(f(w1),f(w2),[hue(i/n),"size->12"]); 
    );  
    ```

1. Push the start button on the right-upper side of the editor.

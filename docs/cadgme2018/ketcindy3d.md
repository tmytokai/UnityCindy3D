---
title: Tutorial
layout: default
---
[Back](./)

# KeTCindy3D

KeTCindy3D is the sets of 3D drawing functions of [KeTCindy](https://sites.google.com/site/ketcindy/english).

KeTCindy3D can generate 3D graphics for TeX documents. It also can generate object files that can be used for a 3D viewer, a 3D printer and so on.

## Getting Started

1. Install the KeTCindy
1. Launch the Cinderella.
1. Open the Script Editor.
1. Open the Initialization slot, and copy the following code.

    ```
    use("KetCindyPlugin"); 
    Dircdy=loaddirectory; 
    setdirectory(plugindirectory); 
    import("dirhead.txt"); 
    Ketinit(); 
    Ketinit3d();
    ```

## How to draw a point

1. Open the Draw slot, and copy the following code.

    ```
    Start3d(); 

    // draws axes
    Putaxes3d(4); 
    Xyzax3data("","x=[-5,5]","y=[-5,5]","z=[-5,5]"); 

    // draws a point  
    Putpoint3d(["A",[2,0,1]],"fix"); 

    Windispg(); 
    ```

1. Push the start button on the right-upper side of the editor.
1. Get back to the main window, where the preview of graphics is shown.
1. Push the Figure button to show the PDF file.

## How to draw a segment

1. Open the Draw slot, and copy the following code.

    ```
    Start3d(); 

    // draws axes
    Putaxes3d(4); 
    Xyzax3data("","x=[-5,5]","y=[-5,5]","z=[-5,5]"); 

    // draws a segment  
    Spaceline("1", [[0,0,0],[2,0,1]]);  

    Windispg(); 
    ```

1. Push the start button on the right-upper side of the editor.
1. Get back to the main window, where the preview of graphics is shown.
1. Push the Figure button to show the PDF file.

## How to draw polygons

1. Open the Draw slot, and copy the following code.

    ```
    Start3d(); 

    // draws axes  
    Putaxes3d(4); 
    Xyzax3data("","x=[-5,5]","y=[-5,5]","z=[-5,5]"); 

    // draws polygons  
    Putpoint3d(["A",[2,0,0]],"fix");  
    Putpoint3d(["B",[0,2,0]],"fix");  
    Putpoint3d(["C",[-2,0,0]],"fix");  
    Putpoint3d(["D",[0,0,2]],"fix");  
    phd = Concatobj([[A,B,D],[B,C,D]]);  
    VertexEdgeFace("1",phd);  
    Nohiddenbyfaces("1","phf3d1",[],["do"]);  
  
    Windispg(); 
    ```

1. Push the start button on the right-upper side of the editor.
1. Get back to the main window, where the preview of graphics is shown.
1. Push the Figure button to show the PDF file.

## How to draw a sphere

1. Open the Draw slot, and copy the following code.
    
    ```
    Start3d(); 

    // draws axes
    Putaxes3d(4); 
    Xyzax3data("","x=[-5,5]","y=[-5,5]","z=[-5,5]"); 

    // draws a sphere
    r=2; 
    fd=["p", 
      "x=r*sin(u)*cos(v)", 
      "y=r*sin(u)*sin(v)", 
      "z=r*cos(u)", 
      "u=[0,pi]","v=[0,2*pi]"];  
    Sf3data("1",fd);  
  
    Windispg(); 
    ```

1. Push the start button on the right-upper side of the editor.
1. Get back to the main window, where the preview of graphics is shown.
1. Push the Figure button to show the PDF file.

## How to draw a curved surface 

1. Open the Draw slot, and copy the following code.

    ```
    Start3d(); 

    // draws axes
    Putaxes3d(4); 
    Xyzax3data("","x=[-5,5]","y=[-5,5]","z=[-5,5]"); 

    // draws a curved surface (saddle surface)  
    fd=["z=(x^2-y^2)/2","x=[-2,2]","y=[-2,2]"];  
    Sf3data("1",fd);  

    Windispg(); 
    ```

1. Push the start button on the right-upper side of the editor.
1. Get back to the main window, where the preview of graphics is shown.
1. Push the Figure button to show the PDF file.

## How to generate an object file of Torus knot

1. Open the Draw slot, and copy the following code.

    ```
    Start3d(); 

    // draws axes
    Putaxes3d(4); 
    Xyzax3data("","x=[-5,5]","y=[-5,5]","z=[-5,5]"); 

    // draws a Torus knot (p,q)
    fd=["sin(8*w),  
      (cos(8*w)+2) * cos(3*w), 
      (cos(8*w)+2) * sin(3*w)"];  
    Spacecurve("1",fd,"w=[0,2*pi]",["Num=200"]);  
  
    Windispg(); 

    // generates object file 
    Mkobjcrvcmd("1","sc3d1", [0.2]);  
    Mkviewobj("torus",oc1,["make"]);  
    ```

1. Push the start button on the right-upper side of the editor.
1. Launch any 3D viewer and open "torus.obj" in the KetCindy's working folder.

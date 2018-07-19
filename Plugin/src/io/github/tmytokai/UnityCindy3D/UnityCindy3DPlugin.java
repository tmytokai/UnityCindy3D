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

package io.github.tmytokai.UnityCindy3D;

import de.cinderella.api.cs.CindyScript;
import de.cinderella.api.cs.CindyScriptPlugin;
import de.cinderella.math.Vec;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Hashtable;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class UnityCindy3DPlugin extends CindyScriptPlugin {

    // copied from Assets/Scripts/Decoder.cs
	private enum COMMAND
	{
                SetCoordinate,
		SelectObject,

		Begin, 
		End,

		SetColor,
		SetRadius,

		AddPoint,
		AddLine,
                AddPolygon,
                AddSphere,
                AddMesh,

		COMMANDSIZE
	};

	private enum MODIFIERS
	{
                Radius,
		Color,
		Colors,
                Topology,

		MODIFIERSSIZE
	};

	private enum COORDINATE
	{
                Zup_RightHand,
		Yup_LeftHand,

		COORDINATESIZE
	};

	private enum TOPOLOGY
	{
		Open,
		Close,

		TOPOLOGYSIZE
	};

    private Hashtable modifiers;

    private Logger logger = null;

    private Socket socket = null;
    private OutputStream output = null;
    private byte[] buffer = null;
    private byte[] modifiers_buffer = null;
    private int buffersize = 1024 * 1024;
    private int port = 8000;
    private String defaultObj = "default";
    private String targetObj;

    public UnityCindy3DPlugin(){
        buffer = new byte[buffersize];
        modifiers_buffer = new byte[buffersize];
        targetObj = defaultObj;
    }

    @Override
    public void register(){}

    @Override
    public void unregister(){
        disconnect();
    }

    @Override
    public String getName(){
        return "UnityCindy3D";
    }

    @Override
    public String getAuthor(){
        return "UnityCindy3D Project";
    }

    @Override
    public void setModifiers( Hashtable m ){
        modifiers = m;
    }

    @CindyScript( "systemproperty3d" )
    public String getSystemProperty3d( String p ) {
        return System.getProperty( p );
    }

    /*
      starts logging
    */
    @CindyScript( "logging3d" )
    public void logging3d( String logfile ){

        try{
            logger = Logger.getLogger( getName() );
            FileHandler handler = new FileHandler( logfile );
            handler.setFormatter( new SimpleFormatter() );
            logger.addHandler( handler );
            DebugLog( "logging3d: started" );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    /*
      sets coordinate system
    */
    @CindyScript( "coordinate3d" )
    public void coordinate3d( String systemname ){

        if( socket == null ) connect();
        if( output == null ) return;

        int chunksize = 1;
        int lng = 0;

        try{
            // command
            buffer[lng] = (byte)COMMAND.SetCoordinate.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // coordinate system
            if( systemname.equals("yup_lefthand") ){ 
                buffer[lng] = (byte)COORDINATE.Yup_LeftHand.ordinal();
            }
            else{
                buffer[lng] = (byte)COORDINATE.Zup_RightHand.ordinal();
            }
            lng++;

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "coordinate3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      prepares drawing
    */
    @CindyScript( "begin3d" )
    public void begin3d( String objname ){

        if( socket == null ) connect();
        if( output == null ) return;

        selectObject( objname );

        int chunksize = 0;
        int lng = 0;

        try{
            // command
            buffer[lng] = (byte)COMMAND.Begin.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "begin3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      prepares drawing
    */
    @CindyScript( "begin3d" )
    public void begin3d(){

        begin3d( defaultObj );
    }

    /*
      finalizes drawing
    */
    @CindyScript( "end3d" )
    public void end3d(){

        if( socket == null || output == null ) return;

        int chunksize = 0;
        int lng = 0;

        try{
            // command
            buffer[lng] = (byte)COMMAND.End.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "end3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      sets the color of all primitives
    */
    @CindyScript( "color3d" )
    public void color3d( ArrayList< Float > color ){

        if( socket == null || output == null ) return;

        if( color.size() != 3 ) {
            String msg = "color3d: color size is not 3";
            DebugLog( msg );
            throw new IllegalArgumentException( msg );
        }

        int chunksize = 4 * 3; // size of float * RGB
        int lng = 0;

        try{
            // command
            buffer[lng] = (byte)COMMAND.SetColor.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // RGB
            for( int i = 0; i < 3; ++i ){
                System.arraycopy( FloatToByte( color.get(i) ), 0, buffer, lng, 4 );
                lng += 4;
            }

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "color3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      sets the size of all primitives
    */
    @CindyScript("size3d")
    public void size3d( float size ){

        if( socket == null || output == null ) return;

        if( size <= 0 ) {
            String msg = "size3d: size is not positive";
            DebugLog( msg );
            throw new IllegalArgumentException( msg );
        }

        int chunksize = 4; // size of float
        int lng = 0;

        try{
            // command
            buffer[lng] = (byte)COMMAND.SetRadius.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // size
            System.arraycopy( FloatToByte( size ), 0, buffer, lng, 4 );
            lng += 4;

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "size3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }
        
    /*
      draws a point
    */
    @CindyScript( "draw3d" )
    public void draw3d( Vec point ){

        if( socket == null || output == null ) return;

        int chunksize = 4 * 3; // size of float * 3 axis
        int lng = 0;

        int modifierssize = applyModifiers();
        chunksize += modifierssize;

        try{

            // command
            buffer[lng] = (byte)COMMAND.AddPoint.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // point
            System.arraycopy( FloatToByte( (float) point.getXR() ), 0, buffer, lng, 4 );
            lng += 4;
            System.arraycopy( FloatToByte( (float) point.getYR() ), 0, buffer, lng, 4 );
            lng += 4;
            System.arraycopy( FloatToByte( (float) point.getZR() ), 0, buffer, lng, 4 );
            lng += 4;

            // modifier
            if( modifierssize > 0 ){
                System.arraycopy( modifiers_buffer, 0, buffer, lng, modifierssize );
                lng += modifierssize;
            }
            
            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "draw3d(point): " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      draws a line
    */
    @CindyScript( "draw3d" )
    public void draw3d( Vec firstPoint, Vec secondPoint ){

        ArrayList<Vec> points = new ArrayList<Vec>();
        points.add( firstPoint );
        points.add( secondPoint );
        colorconnect3d( points, null );
    }

    /*
      Draws a line segments.
    */
    @CindyScript("connect3d")
    public void connect3d( ArrayList<Vec> points ) {
        
        colorconnect3d( points, null );
    }

    /*
      Draws a multicolored line segments.
    */
    @CindyScript("colorconnect3d")
    public void colorconnect3d( ArrayList<Vec> points, ArrayList<Vec> colors ) {

        if( socket == null || output == null ) return;

        if( points == null || points.isEmpty() ) {
            String msg = "colorconnect3d: no points";
            DebugLog( msg );
            throw new IllegalArgumentException( msg );
        }

        int chunksize = 4; // points size
        chunksize += 4 * 3 * points.size(); // size of float * 3 axis * points.size
        if( colors != null ){
            chunksize += 1 + 4; // MODIFIERS.Colors + colors size
            chunksize += 4 * 3 * colors.size(); // size of float * RGB * colors.size
        }
        int lng = 0;

        int modifierssize = applyModifiers();
        chunksize += modifierssize;

        try{
            // command
            buffer[lng] = (byte)COMMAND.AddLine.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // points size
            System.arraycopy( IntToByte( points.size() ), 0, buffer, lng, 4 );
            lng += 4;

            // points
            for (int i = 0; i < points.size(); ++i) {
                System.arraycopy( FloatToByte( (float) points.get(i).getXR() ), 0, buffer, lng, 4 );
                lng += 4;
                System.arraycopy( FloatToByte( (float) points.get(i).getYR() ), 0, buffer, lng, 4 );
                lng += 4;
                System.arraycopy( FloatToByte( (float) points.get(i).getZR() ), 0, buffer, lng, 4 );
                lng += 4;
            }

            // colors
            if( colors != null ){

                buffer[lng] = (byte)MODIFIERS.Colors.ordinal();
                lng++;

                // colors size
                System.arraycopy( IntToByte( colors.size() ), 0, buffer, lng, 4 );
                lng += 4;

                for (int i = 0; i < colors.size(); ++i) {
                    System.arraycopy( FloatToByte( (float) colors.get(i).getXR() ), 0, buffer, lng, 4 );
                    lng += 4;
                    System.arraycopy( FloatToByte( (float) colors.get(i).getYR() ), 0, buffer, lng, 4 );
                    lng += 4;
                    System.arraycopy( FloatToByte( (float) colors.get(i).getZR() ), 0, buffer, lng, 4 );
                    lng += 4;
                }
            }

            // modifier
            if( modifierssize > 0 ){
                System.arraycopy( modifiers_buffer, 0, buffer, lng, modifierssize );
                lng += modifierssize;
            }

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "colorconnect3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      Draws a filled polygon
    */
    @CindyScript( "fillpoly3d" )
    public void fillpoly3d( ArrayList<Vec> points )
    {
        if( socket == null || output == null ) return;

        if( points.size() == 0) {
            String msg = "fillpoly3d: no points";
            DebugLog( msg );
            throw new IllegalArgumentException( msg );
        }

        int chunksize = 4; // points size
        chunksize += 4 * 3 * points.size(); // size of float * 3 axis * points.size
        int lng = 0;

        int modifierssize = applyModifiers();
        chunksize += modifierssize;

        try{
            // command
            buffer[lng] = (byte)COMMAND.AddPolygon.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // points size
            System.arraycopy( IntToByte( points.size() ), 0, buffer, lng, 4 );
            lng += 4;

            // points
            for (int i = 0; i < points.size(); ++i) {
                System.arraycopy( FloatToByte( (float) points.get(i).getXR() ), 0, buffer, lng, 4 );
                lng += 4;
                System.arraycopy( FloatToByte( (float) points.get(i).getYR() ), 0, buffer, lng, 4 );
                lng += 4;
                System.arraycopy( FloatToByte( (float) points.get(i).getZR() ), 0, buffer, lng, 4 );
                lng += 4;
            }

            // modifier
            if( modifierssize > 0 ){
                System.arraycopy( modifiers_buffer, 0, buffer, lng, modifierssize );
                lng += modifierssize;
            }

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "fillpoly3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
      Draws a sphere.
    */
    @CindyScript("drawsphere3d")
    public void drawsphere3d( Vec point, double radius ) {

        if( socket == null || output == null ) return;

        if( radius <= 0 ){
            String msg = "drawsphere3d: radius is not positive";
            DebugLog( msg );
            throw new IllegalArgumentException( msg );
        }

        int chunksize = 4 * 3; // size of float * 3 axis
        chunksize += 1 + 4; // MODIFIERS.Radius + size of float * 1 radius
        int lng = 0;

        int modifierssize = applyModifiers();
        chunksize += modifierssize;

        try{

            // command
            buffer[lng] = (byte)COMMAND.AddSphere.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // center
            System.arraycopy( FloatToByte( (float) point.getXR() ), 0, buffer, lng, 4 );
            lng += 4;
            System.arraycopy( FloatToByte( (float) point.getYR() ), 0, buffer, lng, 4 );
            lng += 4;
            System.arraycopy( FloatToByte( (float) point.getZR() ), 0, buffer, lng, 4 );
            lng += 4;

            // radius
            buffer[lng] = (byte)MODIFIERS.Radius.ordinal();
            lng++;
            System.arraycopy( FloatToByte( (float) radius ), 0, buffer, lng, 4 );
            lng += 4;

            // modifier
            if( modifierssize > 0 ){
                System.arraycopy( modifiers_buffer, 0, buffer, lng, modifierssize );
                lng += modifierssize;
            }
            
            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "drawsphere3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    /*
     Draws a mesh
    */
    @CindyScript("mesh3d")
    public void mesh3d( int rows, int columns, ArrayList<Vec> points ) {

        if( socket == null || output == null ) return;

        if( rows <= 0 || columns <=0 || rows * columns != points.size() ) {
            String msg = "mesh3d: illegal parameters";
            DebugLog( msg );
            throw new IllegalArgumentException( msg );
        }

        int chunksize = 4 + 4 + 4; // rows + columns + points size
        chunksize += 4 * 3 * points.size(); // size of float * 3 axis * points.size
        int lng = 0;

        int modifierssize = applyModifiers();
        chunksize += modifierssize;

        try{
            // command
            buffer[lng] = (byte)COMMAND.AddMesh.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // rows , columns and points size
            System.arraycopy( IntToByte( rows ), 0, buffer, lng, 4 );
            lng += 4;
            System.arraycopy( IntToByte( columns ), 0, buffer, lng, 4 );
            lng += 4;
            System.arraycopy( IntToByte( points.size() ), 0, buffer, lng, 4 );
            lng += 4;

            // points
            for (int i = 0; i < points.size(); ++i) {
                System.arraycopy( FloatToByte( (float) points.get(i).getXR() ), 0, buffer, lng, 4 );
                lng += 4;
                System.arraycopy( FloatToByte( (float) points.get(i).getYR() ), 0, buffer, lng, 4 );
                lng += 4;
                System.arraycopy( FloatToByte( (float) points.get(i).getZR() ), 0, buffer, lng, 4 );
                lng += 4;
            }

            // modifier
            if( modifierssize > 0 ){
                System.arraycopy( modifiers_buffer, 0, buffer, lng, modifierssize );
                lng += modifierssize;
            }

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "mesh3d: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    private boolean connect()
    {
        if( socket != null ) return true;

        try{
            socket = new Socket( "localhost", port );
            output = socket.getOutputStream();
            DebugLog( "connect: connected" );
        } catch ( IOException e ) {
            DebugLog( "connect: " + e.getMessage() );
            disconnect();
            return false;
        }

        return true;
    }

    private void disconnect()
    {
        if( output != null ){

            try{
               output.close();
           } catch ( IOException e ) {
               DebugLog( "disconnect: " + e.getMessage() );
           }

           output = null;
        }

        if( socket != null ){

           try{
               socket.close();
           } catch ( IOException e ) {
               DebugLog( e.getMessage() );
           }

           socket = null;
           DebugLog( "disconnect: disconnected" );
        }
    }

    private void selectObject( String objname ){

        if( output == null ) return;

        if( targetObj != objname ) end3d();

        targetObj = objname;

        int chunksize = Math.min( objname.length(), 256 ); // max 256 strings
        int lng = 0;

        try{
            // command
            buffer[lng] = (byte)COMMAND.SelectObject.ordinal();
            lng++;

            // chunksize
            System.arraycopy( IntToByte( chunksize ), 0, buffer, lng, 4 );
            lng += 4;

            // objname
            System.arraycopy( objname.getBytes(), 0, buffer, lng, chunksize );
            lng += chunksize;

            output.write( buffer, 0, lng );

        } catch ( IOException e ) {
            DebugLog( "selectObject: " + e.getMessage() );
            disconnect();
            throw new RuntimeException( e );
        }
    }

    private int applyModifiers()
    {
        int lng = 0;
        Object value = null;

        value = modifiers.get( "size" );
        if ( value instanceof Double ) {

            modifiers_buffer[lng] = (byte)MODIFIERS.Radius.ordinal();
            lng++;

            double radius = (Double)value;
            System.arraycopy( FloatToByte( (float) radius ), 0, modifiers_buffer, lng, 4 );
            lng += 4;
      	}

        value = modifiers.get( "color" );
        if (value instanceof double[] ) {

            modifiers_buffer[lng] = (byte)MODIFIERS.Color.ordinal();
            lng++;

            double[] color = (double[]) value;
            for( int i = 0; i < 3; ++i ){
                System.arraycopy( FloatToByte( (float) color[i] ), 0, modifiers_buffer, lng, 4 );
                lng += 4;
            }
        }

        value = modifiers.get( "topology" );
        if (value instanceof String ){

            modifiers_buffer[lng] = (byte)MODIFIERS.Topology.ordinal();
            lng++;

            String topology = value.toString();
            if ( topology.equalsIgnoreCase( "close" ) ) {
                modifiers_buffer[lng] = (byte)TOPOLOGY.Close.ordinal();
            }
            else{
                modifiers_buffer[lng] = (byte)TOPOLOGY.Open.ordinal();
            }
            lng++;
        }

        return lng;
    }

    private void DebugLog( String msg ){
        if( logger != null ){
            logger.info( msg );
        }
    }

    private static byte[] FloatToByte( float val )
    {
        ByteBuffer buf = ByteBuffer.allocate( Float.SIZE / Byte.SIZE );
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.putFloat( val ).array();
    }

    private static byte[] IntToByte( int val )
    {
        ByteBuffer buf = ByteBuffer.allocate( Integer.SIZE / Byte.SIZE );
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.putInt( val ).array();
    }
}

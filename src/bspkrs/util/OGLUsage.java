package bspkrs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

/* Thanks Luna!*/
public class OGLUsage
{
    private static final int[]                      ENUMS = new int[] {
                                                          GL11.GL_ALPHA_TEST,
                                                          GL11.GL_AUTO_NORMAL,
                                                          GL11.GL_BLEND,
                                                          GL11.GL_CLIP_PLANE0,
                                                          GL11.GL_CLIP_PLANE1,
                                                          GL11.GL_CLIP_PLANE2,
                                                          GL11.GL_CLIP_PLANE3,
                                                          GL11.GL_CLIP_PLANE4,
                                                          GL11.GL_CLIP_PLANE5,
                                                          GL11.GL_COLOR_ARRAY,
                                                          GL11.GL_COLOR_BUFFER_BIT,
                                                          GL11.GL_COLOR_LOGIC_OP,
                                                          GL11.GL_COLOR_MATERIAL,
                                                          GL11.GL_CULL_FACE,
                                                          GL11.GL_DEPTH_TEST,
                                                          GL11.GL_DITHER,
                                                          GL11.GL_EDGE_FLAG_ARRAY,
                                                          GL11.GL_FOG,
                                                          GL11.GL_INDEX_ARRAY,
                                                          GL11.GL_INDEX_LOGIC_OP,
                                                          GL11.GL_LIGHT0,
                                                          GL11.GL_LIGHT1,
                                                          GL11.GL_LIGHT2,
                                                          GL11.GL_LIGHT3,
                                                          GL11.GL_LIGHT4,
                                                          GL11.GL_LIGHT5,
                                                          GL11.GL_LIGHT6,
                                                          GL11.GL_LIGHT7,
                                                          GL11.GL_LIGHTING,
                                                          GL11.GL_LINE_SMOOTH,
                                                          GL11.GL_LINE_STIPPLE,
                                                          GL11.GL_LOGIC_OP,
                                                          GL11.GL_MAP1_COLOR_4,
                                                          GL11.GL_MAP1_INDEX,
                                                          GL11.GL_MAP1_NORMAL,
                                                          GL11.GL_MAP1_TEXTURE_COORD_1,
                                                          GL11.GL_MAP1_TEXTURE_COORD_2,
                                                          GL11.GL_MAP1_TEXTURE_COORD_3,
                                                          GL11.GL_MAP1_TEXTURE_COORD_4,
                                                          GL11.GL_MAP1_VERTEX_3,
                                                          GL11.GL_MAP1_VERTEX_4,
                                                          GL11.GL_MAP2_COLOR_4,
                                                          GL11.GL_MAP2_INDEX,
                                                          GL11.GL_MAP2_NORMAL,
                                                          GL11.GL_MAP2_TEXTURE_COORD_1,
                                                          GL11.GL_MAP2_TEXTURE_COORD_2,
                                                          GL11.GL_MAP2_TEXTURE_COORD_3,
                                                          GL11.GL_MAP2_TEXTURE_COORD_4,
                                                          GL11.GL_MAP2_VERTEX_3,
                                                          GL11.GL_MAP2_VERTEX_4,
                                                          GL11.GL_NORMAL_ARRAY,
                                                          GL11.GL_NORMALIZE,
                                                          GL11.GL_POINT_SMOOTH,
                                                          GL11.GL_POLYGON_OFFSET_FILL,
                                                          GL11.GL_POLYGON_OFFSET_LINE,
                                                          GL11.GL_POLYGON_OFFSET_POINT,
                                                          GL11.GL_POLYGON_SMOOTH,
                                                          GL11.GL_POLYGON_STIPPLE,
                                                          GL11.GL_SCISSOR_TEST,
                                                          GL11.GL_STENCIL_TEST,
                                                          GL11.GL_TEXTURE_1D,
                                                          GL11.GL_TEXTURE_2D,
                                                          GL11.GL_TEXTURE_COORD_ARRAY,
                                                          GL11.GL_TEXTURE_GEN_Q,
                                                          GL11.GL_TEXTURE_GEN_R,
                                                          GL11.GL_TEXTURE_GEN_S,
                                                          GL11.GL_TEXTURE_GEN_T,
                                                          GL11.GL_VERTEX_ARRAY,
                                                          GL12.GL_RESCALE_NORMAL,
                                                          GL12.GL_TEXTURE_3D,
                                                          GL13.GL_MULTISAMPLE,
                                                          GL13.GL_SAMPLE_ALPHA_TO_COVERAGE,
                                                          GL13.GL_SAMPLE_ALPHA_TO_ONE,
                                                          GL13.GL_SAMPLE_COVERAGE,
                                                          GL13.GL_TEXTURE_CUBE_MAP,
                                                          GL14.GL_COLOR_SUM,
                                                          GL14.GL_FOG_COORDINATE_ARRAY,
                                                          GL14.GL_SECONDARY_COLOR_ARRAY,
                                                          GL15.GL_FOG_COORD_ARRAY,
                                                          GL20.GL_POINT_SPRITE,
                                                          GL20.GL_VERTEX_PROGRAM_POINT_SIZE,
                                                          GL20.GL_VERTEX_PROGRAM_TWO_SIDE,
                                                          GL30.GL_CLIP_DISTANCE0,
                                                          GL30.GL_CLIP_DISTANCE1,
                                                          GL30.GL_CLIP_DISTANCE2,
                                                          GL30.GL_CLIP_DISTANCE3,
                                                          GL30.GL_CLIP_DISTANCE4,
                                                          GL30.GL_CLIP_DISTANCE5,
                                                          GL30.GL_CLIP_DISTANCE6,
                                                          GL30.GL_CLIP_DISTANCE7,
                                                          GL30.GL_FRAMEBUFFER_SRGB,
                                                          GL30.GL_RASTERIZER_DISCARD,
                                                          GL31.GL_PRIMITIVE_RESTART,
                                                          GL31.GL_TEXTURE_RECTANGLE,
                                                          GL32.GL_DEPTH_CLAMP,
                                                          GL32.GL_PROGRAM_POINT_SIZE,
                                                          GL32.GL_SAMPLE_MASK,
                                                          GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS,
                                                          GL40.GL_SAMPLE_SHADING
                                                          };
    private static final Map<String, List<Integer>> START = new HashMap<String, List<Integer>>();
    private static final Map<String, List<Integer>> END   = new HashMap<String, List<Integer>>();
    private static final Stack<String>              KEYS  = new Stack<String>();
    
    private static String enumToString(int e)
    {
        switch (e)
        {
            case GL11.GL_ALPHA_TEST:
                return "GL_ALPHA_TEST [" + Integer.toHexString(e) + "]";
            case GL11.GL_AUTO_NORMAL:
                return "GL_AUTO_NORMAL [" + Integer.toHexString(e) + "]";
            case GL11.GL_BLEND:
                return "GL_BLEND [" + Integer.toHexString(e) + "]";
            case GL11.GL_CLIP_PLANE0:
                return "GL_CLIP_PLANE0 [" + Integer.toHexString(e) + "]";
            case GL11.GL_CLIP_PLANE1:
                return "GL_CLIP_PLANE1 [" + Integer.toHexString(e) + "]";
            case GL11.GL_CLIP_PLANE2:
                return "GL_CLIP_PLANE2 [" + Integer.toHexString(e) + "]";
            case GL11.GL_CLIP_PLANE3:
                return "GL_CLIP_PLANE3 [" + Integer.toHexString(e) + "]";
            case GL11.GL_CLIP_PLANE4:
                return "GL_CLIP_PLANE4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_CLIP_PLANE5:
                return "GL_CLIP_PLANE5 [" + Integer.toHexString(e) + "]";
            case GL11.GL_COLOR_ARRAY:
                return "GL_COLOR_ARRAY [" + Integer.toHexString(e) + "]";
            case GL11.GL_COLOR_LOGIC_OP:
                return "GL_COLOR_LOGIC_OP [" + Integer.toHexString(e) + "]";
            case GL11.GL_COLOR_MATERIAL:
                return "GL_COLOR_MATERIAL [" + Integer.toHexString(e) + "]";
            case GL11.GL_CULL_FACE:
                return "GL_CULL_FACE [" + Integer.toHexString(e) + "]";
            case GL11.GL_DEPTH_TEST:
                return "GL_DEPTH_TEST [" + Integer.toHexString(e) + "]";
            case GL11.GL_DITHER:
                return "GL_DITHER [" + Integer.toHexString(e) + "]";
            case GL11.GL_EDGE_FLAG_ARRAY:
                return "GL_EDGE_FLAG_ARRAY [" + Integer.toHexString(e) + "]";
            case GL11.GL_FOG:
                return "GL_FOG [" + Integer.toHexString(e) + "]";
            case GL11.GL_INDEX_ARRAY:
                return "GL_INDEX_ARRAY [" + Integer.toHexString(e) + "]";
            case GL11.GL_INDEX_LOGIC_OP:
                return "GL_INDEX_LOGIC_OP [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT0:
                return "GL_LIGHT0 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT1:
                return "GL_LIGHT1 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT2:
                return "GL_LIGHT2 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT3:
                return "GL_LIGHT3 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT4:
                return "GL_LIGHT4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT5:
                return "GL_LIGHT5 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT6:
                return "GL_LIGHT6 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHT7:
                return "GL_LIGHT7 [" + Integer.toHexString(e) + "]";
            case GL11.GL_LIGHTING:
                return "GL_LIGHTING [" + Integer.toHexString(e) + "]";
            case GL11.GL_LINE_SMOOTH:
                return "GL_LINE_SMOOTH [" + Integer.toHexString(e) + "]";
            case GL11.GL_LINE_STIPPLE:
                return "GL_LINE_STIPPLE [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_COLOR_4:
                return "GL_MAP1_COLOR_4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_INDEX:
                return "GL_MAP1_INDEX [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_NORMAL:
                return "GL_MAP1_NORMAL [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_TEXTURE_COORD_1:
                return "GL_MAP1_TEXTURE_COORD_1 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_TEXTURE_COORD_2:
                return "GL_MAP1_TEXTURE_COORD_2 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_TEXTURE_COORD_3:
                return "GL_MAP1_TEXTURE_COORD_3 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_TEXTURE_COORD_4:
                return "GL_MAP1_TEXTURE_COORD_4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_VERTEX_3:
                return "GL_MAP1_VERTEX_3 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP1_VERTEX_4:
                return "GL_MAP1_VERTEX_4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_COLOR_4:
                return "GL_MAP2_COLOR_4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_INDEX:
                return "GL_MAP2_INDEX [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_NORMAL:
                return "GL_MAP2_NORMAL [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_TEXTURE_COORD_1:
                return "GL_MAP2_TEXTURE_COORD_1 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_TEXTURE_COORD_2:
                return "GL_MAP2_TEXTURE_COORD_2 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_TEXTURE_COORD_3:
                return "GL_MAP2_TEXTURE_COORD_3 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_TEXTURE_COORD_4:
                return "GL_MAP2_TEXTURE_COORD_4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_VERTEX_3:
                return "GL_MAP2_VERTEX_3 [" + Integer.toHexString(e) + "]";
            case GL11.GL_MAP2_VERTEX_4:
                return "GL_MAP2_VERTEX_4 [" + Integer.toHexString(e) + "]";
            case GL11.GL_NORMAL_ARRAY:
                return "GL_NORMAL_ARRAY [" + Integer.toHexString(e) + "]";
            case GL11.GL_NORMALIZE:
                return "GL_NORMALIZE [" + Integer.toHexString(e) + "]";
            case GL11.GL_POINT_SMOOTH:
                return "GL_POINT_SMOOTH [" + Integer.toHexString(e) + "]";
            case GL11.GL_POLYGON_OFFSET_FILL:
                return "GL_POLYGON_OFFSET_FILL [" + Integer.toHexString(e) + "]";
            case GL11.GL_POLYGON_OFFSET_LINE:
                return "GL_POLYGON_OFFSET_LINE [" + Integer.toHexString(e) + "]";
            case GL11.GL_POLYGON_OFFSET_POINT:
                return "GL_POLYGON_OFFSET_POINT [" + Integer.toHexString(e) + "]";
            case GL11.GL_POLYGON_SMOOTH:
                return "GL_POLYGON_SMOOTH [" + Integer.toHexString(e) + "]";
            case GL11.GL_POLYGON_STIPPLE:
                return "GL_POLYGON_STIPPLE [" + Integer.toHexString(e) + "]";
            case GL11.GL_SCISSOR_TEST:
                return "GL_SCISSOR_TEST [" + Integer.toHexString(e) + "]";
            case GL11.GL_STENCIL_TEST:
                return "GL_STENCIL_TEST [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_1D:
                return "GL_TEXTURE_1D [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_2D:
                return "GL_TEXTURE_2D [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_COORD_ARRAY:
                return "GL_TEXTURE_COORD_ARRAY [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_GEN_Q:
                return "GL_TEXTURE_GEN_Q [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_GEN_R:
                return "GL_TEXTURE_GEN_R [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_GEN_S:
                return "GL_TEXTURE_GEN_S [" + Integer.toHexString(e) + "]";
            case GL11.GL_TEXTURE_GEN_T:
                return "GL_TEXTURE_GEN_T [" + Integer.toHexString(e) + "]";
            case GL11.GL_VERTEX_ARRAY:
                return "GL_VERTEX_ARRAY [" + Integer.toHexString(e) + "]";
            case GL12.GL_RESCALE_NORMAL:
                return "GL_RESCALE_NORMAL [" + Integer.toHexString(e) + "]";
            case GL12.GL_TEXTURE_3D:
                return "GL_TEXTURE_3D [" + Integer.toHexString(e) + "]";
            case GL13.GL_MULTISAMPLE:
                return "GL_MULTISAMPLE [" + Integer.toHexString(e) + "]";
            case GL13.GL_SAMPLE_ALPHA_TO_COVERAGE:
                return "GL_SAMPLE_ALPHA_TO_COVERAGE [" + Integer.toHexString(e) + "]";
            case GL13.GL_SAMPLE_ALPHA_TO_ONE:
                return "GL_SAMPLE_ALPHA_TO_ONE [" + Integer.toHexString(e) + "]";
            case GL13.GL_SAMPLE_COVERAGE:
                return "GL_SAMPLE_COVERAGE [" + Integer.toHexString(e) + "]";
            case GL13.GL_TEXTURE_CUBE_MAP:
                return "GL_TEXTURE_CUBE_MAP [" + Integer.toHexString(e) + "]";
            case GL14.GL_COLOR_SUM:
                return "GL_COLOR_SUM [" + Integer.toHexString(e) + "]";
            case GL14.GL_FOG_COORDINATE_ARRAY:
                return "GL_FOG_COORDINATE_ARRAY [" + Integer.toHexString(e) + "]";
            case GL14.GL_SECONDARY_COLOR_ARRAY:
                return "GL_SECONDARY_COLOR_ARRAY [" + Integer.toHexString(e) + "]";
            case GL20.GL_POINT_SPRITE:
                return "GL_POINT_SPRITE [" + Integer.toHexString(e) + "]";
            case GL20.GL_VERTEX_PROGRAM_POINT_SIZE:
                return "GL_VERTEX_PROGRAM_POINT_SIZE [" + Integer.toHexString(e) + "]";
            case GL20.GL_VERTEX_PROGRAM_TWO_SIDE:
                return "GL_VERTEX_PROGRAM_TWO_SIDE [" + Integer.toHexString(e) + "]";
            case GL30.GL_CLIP_DISTANCE6:
                return "GL_CLIP_DISTANCE6 [" + Integer.toHexString(e) + "]";
            case GL30.GL_CLIP_DISTANCE7:
                return "GL_CLIP_DISTANCE7 [" + Integer.toHexString(e) + "]";
            case GL30.GL_FRAMEBUFFER_SRGB:
                return "GL_FRAMEBUFFER_SRGB [" + Integer.toHexString(e) + "]";
            case GL30.GL_RASTERIZER_DISCARD:
                return "GL_RASTERIZER_DISCARD [" + Integer.toHexString(e) + "]";
            case GL31.GL_PRIMITIVE_RESTART:
                return "GL_PRIMITIVE_RESTART [" + Integer.toHexString(e) + "]";
            case GL31.GL_TEXTURE_RECTANGLE:
                return "GL_TEXTURE_RECTANGLE [" + Integer.toHexString(e) + "]";
            case GL32.GL_DEPTH_CLAMP:
                return "GL_DEPTH_CLAMP [" + Integer.toHexString(e) + "]";
            case GL32.GL_SAMPLE_MASK:
                return "GL_SAMPLE_MASK [" + Integer.toHexString(e) + "]";
            case GL32.GL_TEXTURE_CUBE_MAP_SEAMLESS:
                return "GL_TEXTURE_CUBE_MAP_SEAMLESS [" + Integer.toHexString(e) + "]";
            case GL40.GL_SAMPLE_SHADING:
                return "GL_SAMPLE_SHADING [" + Integer.toHexString(e) + "]";
            default:
                return "? [" + Integer.toHexString(e) + "]";
        }
    }
    
    public static void clear()
    {
        START.clear();
        END.clear();
        KEYS.clear();
        if (--c < 0)
        {
            c = 100;
            //System.out.println();
        }
    }
    
    private static int c = 0;
    
    private static void capture(Map<String, List<Integer>> map)
    {
        List<Integer> enums = new ArrayList<Integer>();
        
        for (int e : ENUMS)
        {
            if (GL11.glIsEnabled(e))
            {
                if (!enums.contains(e))
                {
                    enums.add(e);
                }
            }
        }
        
        if (c == 0)
        {
            // System.out.println(key());
        }
        
        map.put(key(), enums);
    }
    
    private static String key()
    {
        String str = "";
        for (int i = 0; i < KEYS.size(); i++)
        {
            String key = KEYS.get(i);
            if (str.length() == 0)
                str += key;
            else
                str += "." + key;
        }
        return str;
    }
    
    public static void startCapture(String key)
    {
        KEYS.push(key.replace(".", "_"));
        capture(START);
    }
    
    public static void endCapture()
    {
        capture(END);
        if (KEYS.size() > 0)
            KEYS.pop();
    }
    
    public static void endStartCapture(String key)
    {
        endCapture();
        startCapture(key);
    }
    
    public static void dump()
    {
        for (int e : ENUMS)
        {
            if (GL11.glIsEnabled(e))
            {
                System.out.println(enumToString(e));
            }
        }
    }
    
    public static List<String> diff()
    {
        List<String> list = new ArrayList<String>();
        Set<String> strings = START.keySet();
        List<String> li = new ArrayList<String>(strings);
        Collections.sort(li);
        for (String key : li)
        {
            if (!END.containsKey(key))
            {
                list.add("key mismatch (" + key + ")!");
            }
            else
            {
                list.add(key + ":");
                List<Integer> start = START.get(key);
                List<Integer> end = END.get(key);
                
                if (start == null || end == null)
                    continue;
                
                Collections.sort(start);
                Collections.sort(end);
                
                int count = 0;
                for (Integer i : start)
                {
                    if (!end.contains(i))
                    {
                        list.add("< " + enumToString(i));
                        count++;
                    }
                }
                
                for (Integer i : end)
                {
                    if (!start.contains(i))
                    {
                        list.add("> " + enumToString(i));
                        count++;
                    }
                }
                
                if (count == 0)
                    list.remove(list.size() - 1);
            }
        }
        return list;
    }
    
    public static List<String> all()
    {
        List<String> list = new ArrayList<String>();
        Set<String> strings = START.keySet();
        List<String> li = new ArrayList<String>(strings);
        Collections.sort(li);
        for (String key : li)
        {
            if (!END.containsKey(key))
            {
                list.add("key mismatch (" + key + ")!");
            }
            else
            {
                list.add(key + ":");
                List<Integer> start = START.get(key);
                List<Integer> end = END.get(key);
                
                if (start == null || end == null)
                    return list;
                
                for (Integer i : start)
                {
                    list.add("< " + enumToString(i));
                }
                
                for (Integer i : end)
                {
                    list.add("> " + enumToString(i));
                }
            }
        }
        return list;
    }
}
package a.polverini.my;

import android.app.*;
import android.os.*;
import android.graphics.drawable.shapes.*;
import android.graphics.*;
import java.util.*;
import android.graphics.drawable.*;
import android.view.*;
import android.content.*;
import a.polverini.my.MainActivity.*;
import org.apache.http.cookie.*;

public class MainActivity extends Activity 
{
	
	public static class MyShape extends Shape
	{
		private String id;
		private float x = 0;
		private float y = 0;
		private int size = 30;
		private int value = 0;
		private Set<Integer> options;
		private Set<MyShape> box;
		private Set<MyShape> row;
		private Set<MyShape> col;

		public Set<MyShape> getLinkedRow()
		{
			return row;
		}

		public void setOption(int option, boolean set)
		{
			if(set) {
				if(!options.contains(option)) {
					options.add(option);
				}
			} else {
				if(options.contains(option)) {
					options.remove(option);
				}
			}
		}

		public int getValue() {
			return value;
		}
		
		public Set<Integer> getOptions() {
			return options;
		}

		public void clear() {
			options.clear();
			for(int i=1;i<=9;i++) {
				options.add(i);
			}
			value = 0;
		}
		
		public void update() {
			if(!box.isEmpty()) {
				Iterator<MyShape> i = box.iterator();
				while(i.hasNext()) {
					MyShape other = i.next();
					
						if(other.options.size()==1) {
							int otherValue = other.getValue();
							if(options.contains(otherValue)) {
								options.remove(otherValue);
							}
						}
					
				}
			}

			if(!row.isEmpty()) {
				Iterator<MyShape> i = row.iterator();
				while(i.hasNext()) {
					MyShape other = i.next();
					
						if(other.options.size()==1) {
							int otherValue = other.getValue();
							if(options.contains(otherValue)) {
								options.remove(otherValue);
							}
						}
					
				}
			}

			if(!col.isEmpty()) {
				Iterator<MyShape> i = col.iterator();
				while(i.hasNext()) {
					MyShape other = i.next();
					
						if(other.options.size()==1) {
							int otherValue = other.getValue();
							if(options.contains(otherValue)) {
								options.remove(otherValue);
							}
						}
					
				}
			}
			
			if(options.size()>1 && !box.isEmpty()) {
				
				Set<Integer> certain = new HashSet<>();
				certain.addAll(options);
				
				Iterator<MyShape> i = box.iterator();
				while(i.hasNext()) {
					MyShape other = i.next();
					
						for(int option : options) {
							if(other.getOptions().contains(option)) {
								certain.remove(option);
							}
						}
					
				}
				
				if(certain.size()==1) {
					options.clear();
					options.addAll(certain);
				}
			}

			if(options.size()>1 && !row.isEmpty()) {

				Set<Integer> certain = new HashSet<>();
				certain.addAll(options);

				Iterator<MyShape> i = row.iterator();
				while(i.hasNext()) {
					MyShape other = i.next();
					
						for(int option : options) {
							if(other.getOptions().contains(option)) {
								certain.remove(option);
							}
						}
					
				}

				if(certain.size()==1) {
					options.clear();
					options.addAll(certain);
				}
			}

			if(options.size()>1 && !col.isEmpty()) {

				Set<Integer> certain = new HashSet <>();
				certain.addAll(options);

				Iterator<MyShape> i = col.iterator();
				while(i.hasNext()) {
					MyShape other = i.next();
					
						for(int option : options) {
							if(other.getOptions().contains(option)) {
								certain.remove(option);
							}
						}
					
				}

				if(certain.size()==1) {
					options.clear();
					options.addAll(certain);
				}
			}

			if(options.size()==1) {
				value = options.toArray()[0];
				return;
			}
		}

		public void set(int value)
		{
			this.value = value;
			this.options.clear();
			this.options.add(value);
		}
		
		public void linBox(MyShape other)
		{
			if(!box.contains(other)) {
				box.add(other);
			}
		}
		
		public void linkCol(MyShape other)
		{
			if(!col.contains(other)) {
				col.add(other);
			}
		}

		public void linkRow(MyShape other)
		{
			if(!row.contains(other)) {
				row.add(other);
			}
		}
		
		public boolean isSameRow(MyShape other) {
			return this.id.substring(0,1).equals(other.id.substring(0,1));
		}

		public boolean isSameCol(MyShape other) {
			return this.id.substring(2,3).equals(other.id.substring(2,3));
		}
		
		public MyShape(String id) {
			this.id = id;
			options = new HashSet<>();
			box = new HashSet<>();
			row = new HashSet<>();
			col = new HashSet<>();
			clear();
		}

		@Override
		public void draw(Canvas canvas, Paint paint)
		{
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(0.0f, 0.0f, 3*size, 3*size, paint);
			
			paint.setTextSize(size);
			for(int x=0;x<3;x++) {
				for(int y=0;y<3;y++) {
					int digit = 1+x+(y*3);
					if(options.contains(digit)) {
						if(options.size()==1) {
							paint.setColor(0xFF208020);
						} else {
							paint.setColor(0xFF802020);
						}
						canvas.drawText(""+digit, 3+this.x+(x*size), this.y+((1+y)*size)-3, paint);
					}
				}
			}
			
			if(value!=0) {
				paint.setTextSize(size*3);
				canvas.drawText(""+value, 12+this.x, this.y+(size*3)-9, paint);
			}
	
		}

		public static ShapeDrawable create(String id, int x, int y, int width, int height, int color)
		{
			MyShape shape = new MyShape(id);
			ShapeDrawable drawable;
			drawable = new ShapeDrawable(shape);
			drawable.getPaint().setColor(color);
			drawable.setBounds(x, y, x + width, y + height);
			return(drawable);
		}
		
	}
	
	public static class MyGroup extends Shape
	{
		private Map<String, ShapeDrawable> drawables = new HashMap<>();
		private String id;
		
		public boolean isSolved() {
			for(ShapeDrawable drawable : drawables.values()) {
				if(((MyShape)drawable.getShape()).getValue() == 0) {
					return false;
				}
			}
			return true;
		}
		
		public void clear() {
			for(ShapeDrawable drawable : drawables.values()) {
				((MyShape)drawable.getShape()).clear();
			}
		}
		
		public void update() {
			for(ShapeDrawable drawable : drawables.values()) {
				((MyShape)drawable.getShape()).update();
			}
		}
		
		public void set(String shapeid, int value)
		{
			((MyShape)(drawables.get(shapeid).getShape())).set(value);
		}
		
		public void linkCol(MyGroup otherGroup)
		{
			for(ShapeDrawable drawable : drawables.values()) {
				MyShape thisShape = (MyShape)drawable.getShape();
				for(ShapeDrawable other : otherGroup.drawables.values()) {
					if(other!=drawable) {
						MyShape otherShape = (MyShape)other.getShape();
						if(otherShape.isSameCol(thisShape)) {
							thisShape.linkCol(otherShape);
						}
					}
				}
			}
		}

		public void linkRow(MyGroup otherGroup)
		{
			for(ShapeDrawable drawable : drawables.values()) {
				MyShape thisShape = (MyShape)drawable.getShape();
				for(ShapeDrawable other : otherGroup.drawables.values()) {
					if(other!=drawable) {
						MyShape otherShape = (MyShape)other.getShape();
						if(otherShape.isSameRow(thisShape)) {
							thisShape.linkRow(otherShape);
						}
					}
				}
			}
		}
		
		public ShapeDrawable getDrawable(String name) {
			return drawables.get(name);
		}
		
		public boolean isSameRow(MyGroup other) {
			return this.id.substring(0,1).equals(other.id.substring(0,1));
		}
		
		public boolean isSameCol(MyGroup other) {
			return this.id.substring(2,3).equals(other.id.substring(2,3));
		}
		
		public MyGroup(String id) {
			this.id = id;
			for(int x=0;x<3;x++) {
				for(int y=0;y<3;y++) {
					String shapeid = String.format("%d.%d",y,x);
					ShapeDrawable shape = MyShape.create(shapeid, 10 + x * 90, 20 + y * 90, 90, 90, 0xff232374);
					drawables.put(shapeid, shape);
				}
			}
			
			for(ShapeDrawable drawable : drawables.values()) {
				MyShape thisShape = ((MyShape)(drawable.getShape()));
				for (ShapeDrawable other : drawables.values()) {
					if (other != drawable) {
						MyShape otherShape = ((MyShape)(other.getShape()));
						thisShape.linBox(otherShape);
					}
				}
			}
			
		}

		@Override
		public void draw(Canvas canvas, Paint paint)
		{
			for(ShapeDrawable drawable : drawables.values()) {
				drawable.draw(canvas);
			}
		}

		public static ShapeDrawable create(String id, int x, int y, int width, int height, int color)
		{
			MyGroup group = new MyGroup(id);
			ShapeDrawable drawable;
			drawable = new ShapeDrawable(group);
			drawable.getPaint().setColor(color);
			drawable.setBounds(x, y, x + width, y + height);
			return(drawable);
		}
		
	}
	
	public class DrawableView extends View {

		private Map<String, ShapeDrawable> drawables = new HashMap<>();
		
		public boolean isSolved() {
			for(ShapeDrawable drawable : drawables.values()) {
				if(((MyGroup)drawable.getShape()).isSolved() == false) {
					return false;
				}
			}
			return true;
		}
		
		public void clear() {
			for(ShapeDrawable drawable : drawables.values()) {
				((MyGroup)drawable.getShape()).clear();
			}
		}
		
		public void update() {
			for(ShapeDrawable drawable : drawables.values()) {
				((MyGroup)drawable.getShape()).update();
			}
		}

		public ShapeDrawable getDrawable(String name) {
			return drawables.get(name);
		}
		
		public DrawableView(Context context) {
			super(context);
			for(int x=0;x<3;x++) {
				for(int y=0;y<3;y++) {
					String id = String.format("%d.%d",y,x);
					ShapeDrawable shape = MyGroup.create(id, 10+x*300, 20+y*300, 300, 300, 0xff232374);
					drawables.put(id, shape);
				}
			}
			
			for(ShapeDrawable drawable : drawables.values()) {
				MyGroup thisGroup = (MyGroup)drawable.getShape();
				for(ShapeDrawable other : drawables.values()) {
					MyGroup otherGroup = (MyGroup)other.getShape();
					if(otherGroup.isSameRow(thisGroup)) {
						thisGroup.linkRow(otherGroup);
					}
					if(otherGroup.isSameCol(thisGroup)) {
						thisGroup.linkCol(otherGroup);
					}
				}
			}

		}

		protected void onDraw(Canvas canvas) {
			for(ShapeDrawable drawable : drawables.values()) {
				drawable.draw(canvas);
			}
		}
		
		public void set(String groupid, String shapeid, int value) {
			((MyGroup)(drawables.get(groupid).getShape())).set(shapeid, value);
		}
		
		public MyShape getShape(int row, int col) {
			String groupid = String.format("%d.%d",row/3,col/3);
			String shapeid = String.format("%d.%d",row%3,col%3);
			MyGroup group = (MyGroup)(getDrawable(groupid).getShape());
			MyShape shape = (MyShape)(group.getDrawable(shapeid).getShape());
			return shape;
		}
	}

	private DrawableView view;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        view = new DrawableView(this);
		setContentView(view);
		
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... args) {
				try {
					Thread.sleep(5000);
					view.clear();
					example1();
					while(true) {
						view.update();
						view.postInvalidate();
						Thread.sleep(1000);
						if(view.isSolved()) {
							break;
						}
					}
					view.clear();
					example2();
					while(true) {
						view.update();
						view.postInvalidate();
						Thread.sleep(1000);
						if(view.isSolved()) {
							break;
						}
					}
				} catch(Exception e) {}
				return null;
			}
		}.execute();
		
    }
	
	public void example1() {
		view.set("0.0","0.1",7);
		view.set("0.0","1.0",8);
		view.set("0.0","2.2",6);

		view.set("0.1","0.1",2);
		view.set("0.1","1.0",7);
		view.set("0.1","1.2",1);

		view.set("0.2","0.1",4);
		view.set("0.2","1.2",9);
		view.set("0.2","2.0",3);

		view.set("1.0","0.1",8);
		view.set("1.0","1.2",5);

		view.set("1.1","0.2",7);
		view.set("1.1","2.0",4);

		view.set("1.2","1.0",4);
		view.set("1.2","2.1",9);

		view.set("2.0","0.2",7);
		view.set("2.0","1.0",2);
		view.set("2.0","2.1",1);

		view.set("2.1","1.0",5);
		view.set("2.1","1.2",6);
		view.set("2.1","2.1",3);

		view.set("2.2","0.0",9);
		view.set("2.2","1.2",1);
		view.set("2.2","2.1",2);
	}
	
	public void example2() {
		view.set("0.0","1.2",9);
		view.set("0.0","2.1",6);

		view.set("0.1","0.0",8);
		view.set("0.1","1.2",3);
		view.set("0.1","2.1",9);

		view.set("0.2","0.0",1);
		view.set("0.2","1.2",7);
		view.set("0.2","2.1",4);

		view.set("1.0","0.0",2);
		view.set("1.0","1.1",1);
		view.set("1.0","2.2",3);

		view.set("1.1","0.0",4);
		view.set("1.1","2.2",8);

		view.set("1.2","0.0",8);
		view.set("1.2","1.1",9);
		view.set("1.2","2.2",4);

		view.set("2.0","0.1",7);
		view.set("2.0","1.0",1);
		view.set("2.0","2.2",4);

		view.set("2.1","0.1",8);
		view.set("2.1","1.0",5);
		view.set("2.1","2.2",7);

		view.set("2.2","0.1",5);
		view.set("2.2","1.0",2);
	}
	
}

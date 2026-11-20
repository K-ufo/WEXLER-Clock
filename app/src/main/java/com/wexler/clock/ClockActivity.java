package com.wexler.clock;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.content.Context;
import android.graphics.*;
import java.util.Calendar;
public class ClockActivity extends Activity {
 @Override public void onCreate(Bundle b) {
  super.onCreate(b);
  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
  setContentView(new FlipView(this));
 }
 @Override public void onWindowFocusChanged(boolean focused) {
  super.onWindowFocusChanged(focused);
  if(focused) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE);
 }
 static class FlipView extends View {
  Paint p = new Paint(3);
  FlipView(Context c) { super(c); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }
  void rect(Canvas c,int color,float l,float t,float r,float b,float radius) {
   p.setColor(color);p.setStyle(Paint.Style.FILL);c.drawRoundRect(new RectF(l,t,r,b),radius,radius,p);
  }
  void card(Canvas c,String digits,float x,float y,float w,float h,float scale) {
   rect(c,Color.rgb(17,17,17),x,y,x+w,y+h,22*scale);
   p.setTypeface(Typeface.create("sans-serif-condensed",Typeface.BOLD));
   p.setTextAlign(Paint.Align.CENTER);p.setTextSize(h*0.94f);p.setColor(Color.rgb(188,188,188));
   Paint.FontMetrics fm=p.getFontMetrics();
   float baseline=y+h/2f-(fm.ascent+fm.descent)/2f;
   c.save();c.clipRect(x+3*scale,y+2*scale,x+w-3*scale,y+h-2*scale);
   c.drawText(digits,x+w/2f,baseline,p);c.restore();
   rect(c,Color.BLACK,x,y+h/2f-2*scale,x+w,y+h/2f+2*scale,0);
   rect(c,Color.rgb(10,10,10),x+2*scale,y+h/2f+3*scale,x+w-2*scale,y+h/2f+4*scale,0);
  }
  @Override protected void onDraw(Canvas canvas) {
   super.onDraw(canvas);canvas.drawColor(Color.BLACK);
   Calendar now=Calendar.getInstance();
   int hour=now.get(Calendar.HOUR_OF_DAY),minute=now.get(Calendar.MINUTE);
   float W=getWidth(),H=getHeight(),gap=W*0.024f;
   float cw=Math.min((W*0.88f-gap)/2f,H*0.82f*1.02f);
   float ch=Math.min(H*0.82f,cw*0.98f);cw=Math.min(cw,ch*1.08f);
   float x=(W-2*cw-gap)/2f,y=(H-ch)/2f,scale=Math.min(W/1024f,H/600f);
   card(canvas,String.format(java.util.Locale.US,"%02d",hour),x,y,cw,ch,scale);
   card(canvas,String.format(java.util.Locale.US,"%02d",minute),x+cw+gap,y,cw,ch,scale);
   postInvalidateDelayed(500);
  }
 }
}

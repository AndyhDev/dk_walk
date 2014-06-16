package com.dk.walk.util;

import com.dk.walk.database.SQLWay;
import android.widget.TextView;

public class WayHolder{
    public TextView title;
    public TextView date;
    public TextView way;
    public boolean expanded = false;
    public int position;
    public SQLWay sqlWay;
}

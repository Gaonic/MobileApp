package com.gaonic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SensorsAdapter extends BaseExpandableListAdapter implements Filterable{

	private MainActivity _context;
	private List<HashMap<String,Object>> sData;
	private int expanded = -1;

	@Override
	public void onGroupExpanded(int groupPosition) {
		if (onGroupExpanded != null)
			onGroupExpanded.onGroupExpanded(groupPosition);
		expanded = groupPosition;
		super.onGroupExpanded(groupPosition);
		notifyDataSetChanged();

	}
	
	public List<HashMap<String,Object>> getData() {
		return sData;
	}
	public interface OnGroupExpandedListener {
		public void onGroupExpanded(int groupPosition);
	}
	public void setOnGroupExpanded(OnGroupExpandedListener onGroupExpanded) {
		this.onGroupExpanded = onGroupExpanded;
	}
	private OnGroupExpandedListener onGroupExpanded;
	private boolean bubbleMode;
	private OnClickListener navigateToListener;
	
	public SensorsAdapter(MainActivity context,List<HashMap<String,Object>> sData) {
		this._context = context;
		this.sData = sData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return getGroupCount() > 0 ? sData.get(groupPosition) : null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public void setBubbleMode(boolean bMode) {
		this.bubbleMode= bMode;
	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.sensor_item, null);
		}

		TableLayout txtListChild = (TableLayout) convertView
				.findViewById(R.id.sensor_data_tb);

		if (expanded == groupPosition) {
			buildTable(txtListChild, (HashMap<String, Object>) getChild(groupPosition, childPosition),false);
		}
		return convertView;
	}
	
	public void setExpanded(int e) {
		expanded = e;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return getGroupCount() > 0 ? sData.get(groupPosition).get("name") : "";
	}

	@Override
	public int getGroupCount() {
		return sData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.sensor_group_item, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		ImageView colExp = (ImageView) convertView.findViewById(R.id.colExpBtn);
		if (isExpanded) {
			colExp.setImageResource(R.drawable.moreinfo_arrow_expanded);
		} else {
			colExp.setImageResource(R.drawable.moreinfo_arrow_collapsed);
			
		}
		ImageView navigateTo = (ImageView) convertView.findViewById(R.id.navigate_to);
		if (navigateToListener != null)
			navigateTo.setOnClickListener(navigateToListener);
		if (bubbleMode) {
			navigateTo.setVisibility(View.GONE);
		}
		convertView.findViewById(R.id.details).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GaonicDialog dialog = new GaonicDialog(_context,null) {
					
					public void onStart() {
						super.onStart();
						View body =  ((LayoutInflater)_context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sensor_item, null);
					

					TableLayout txtListChild = (TableLayout) body
							.findViewById(R.id.sensor_data_tb);

					buildTable(txtListChild, (HashMap<String, Object>) sData.get(groupPosition),true);
					
					dialogContentView.addView(body);
					}
				};
				dialog.setTitle(headerTitle);
				dialog.setDismissOnButtonAction(true);
				dialog.setPositiveButton("Close", null);
				dialog.show();
			}
			
		});
		convertView.findViewById(R.id.notes).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SensorNotesDialog(_context, null, (String)sData.get(groupPosition).get("name"), (Integer)sData.get(groupPosition).get("id")).show();
			}
		});
		return convertView;
	}

	public void setOnNaviageButtonClicked(View.OnClickListener lis) {
		this.navigateToListener = lis;
	}
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private String [] info = new String [] {"name","id","longitude","latitude"};
	
	protected void buildTable(TableLayout table,Map<String,Object> sData,boolean details) {
		if (sData == null) {
			return;
		}
		table.removeAllViews();
		if(details) {
		TableRow headerRow =new TableRow(_context);
		headerRow.setBackgroundColor(Color.parseColor("#ABC854"));
		headerRow.setPadding(5, 5, 5, 5); //Border between rows

		TextView tv1=new TextView(_context);
		//tv1.setBackgroundResource(R.drawable.table_header_shape);
		tv1.setGravity(Gravity.LEFT);
		tv1.setTextColor(Color.WHITE);
		tv1.setPadding(5, 5, 5, 5);
		tv1.setText("sensor Info");
		tv1.setTypeface(null, Typeface.BOLD);
		headerRow.addView(tv1);
		
		table.addView(headerRow);

		for(int i = 1; i < info.length;i++) {
			TableRow tbrow=new TableRow(_context);
			tbrow.setBackgroundColor(Color.WHITE);
			tbrow.setPadding(0, 0, 0, 1); //Border between rows
			
			TextView tv1_=new TextView(_context);
			//tv1_.setBackgroundResource(R.drawable.table_cell_shape);
			tv1_.setTextColor(Color.parseColor("#171717"));
			tv1_.setPadding(5, 5, 5, 5);
			tv1_.setText(change(info[i]));
			tbrow.addView(tv1_);
			
			TextView tv1__=new TextView(_context);
			//tv1__.setBackgroundResource(R.drawable.table_cell_shape);
			tv1__.setTextColor(Color.parseColor("#171717"));
			tv1__.setPadding(5, 5, 5, 5);
			tv1__.setText(change(sData.get(info[i]).toString()));
			tbrow.addView(tv1__);
		
			table.addView(tbrow);
			}
		
		}
		TableRow headerRow =new TableRow(_context);
		headerRow.setBackgroundColor(Color.parseColor("#ABC854"));
		headerRow.setPadding(5, 5, 5, 5); //Border between rows

		TextView tv1=new TextView(_context);
		//tv1.setBackgroundResource(R.drawable.table_header_shape);
		tv1.setGravity(Gravity.LEFT);
		tv1.setTextColor(Color.WHITE);
		tv1.setPadding(5, 5, 5, 5);
		tv1.setText("Name");
		tv1.setTypeface(null, Typeface.BOLD);
		headerRow.addView(tv1);
		
		tv1=new TextView(_context);
		//tv1.setBackgroundResource(R.drawable.table_header_shape);
		tv1.setGravity(Gravity.LEFT);
		tv1.setTextColor(Color.WHITE);
		tv1.setPadding(5, 5, 5, 5);
		tv1.setText("Value");
		tv1.setTypeface(null, Typeface.BOLD);
		headerRow.addView(tv1);
		
		table.addView(headerRow);
		
		addToTable(table, sData);
	}

	
	private void addToTable(TableLayout table,Map<String,Object> sData ) {
		for(Map.Entry<String, Object> e :sData.entrySet()) {
			boolean cont = false;
			if (e.getValue() != null) {
				for (String s : info) {
					if (s.equals(e.getKey())) {
						cont = true;
						break;
					}
				}
				if (cont) {
					continue;
				}
				Log.v("Class", e.getValue().getClass()+"");
				
				if(e.getValue() instanceof ArrayList<?>) {
					if(!((ArrayList<?>)e.getValue()).isEmpty())
						Log.v("ClassInner", ((ArrayList<?>)e.getValue()).get(0).getClass()+"");
						for (Object o : ((ArrayList<?>)e.getValue())) {
							if(o instanceof Map<?,?>)
								addToTable(table, (Map<String,Object>)o);
						}
				} else {
					TableRow tbrow=new TableRow(_context);
					tbrow.setBackgroundColor(Color.WHITE);
					tbrow.setPadding(0, 0, 0, 1); //Border between rows
					
					TextView tv1_=new TextView(_context);
					//tv1_.setBackgroundResource(R.drawable.table_cell_shape);
					tv1_.setTextColor(Color.parseColor("#171717"));
					tv1_.setPadding(5, 5, 5, 5);
					tv1_.setText(change(e.getKey().toString()));
					tbrow.addView(tv1_);
				
					TextView tv1__=new TextView(_context);
					//tv1__.setBackgroundResource(R.drawable.table_cell_shape);
					tv1__.setTextColor(Color.parseColor("#171717"));
					tv1__.setPadding(5, 5, 5, 5);
					tv1__.setText(change(sData.get(e.getKey().toString()).toString()));
					tbrow.addView(tv1__);
					
					table.addView(tbrow);
				}
			}
		}
		

	}
	public static String change(String word) {
         /*final StringBuilder result = new StringBuilder(word.length());
        String[] words = word.split("_");
        for(int i=0,l=words.length;i<l;++i) {
        	if(i>0) result.append(" ");      
        	result.append(Character.toUpperCase(words[i].charAt(0)))
        	.append(words[i].substring(1));
        }*/
		return (word+"").toUpperCase().toString();

    }

	@Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

               sData = (List<HashMap<String, Object>>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	FilterResults results = new FilterResults();
                List<HashMap<String, Object>> FilteredArrayNames = new ArrayList<HashMap<String, Object>>();

                constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < getGroupCount(); i++) {
                        String dataNames = getGroup(i).toString();
                        if (dataNames.toLowerCase().contains(constraint.toString()))  {
                            FilteredArrayNames.add(sData.get(i));
                        }
                    }

                    results.count = FilteredArrayNames.size();
                    results.values = FilteredArrayNames;
                    

                return results;
        
            }
        };

	
        return filter;

    }
}
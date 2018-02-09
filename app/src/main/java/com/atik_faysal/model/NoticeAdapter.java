package com.atik_faysal.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atik_faysal.backend.InfoBackgroundTask;
import com.atik_faysal.mealcounter.NeedSomeMethod;
import com.atik_faysal.mealcounter.NoticeBoard;
import com.atik_faysal.mealcounter.R;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.atik_faysal.backend.InfoBackgroundTask.OnAsyncTaskInterface;
/**
 * Created by USER on 2/9/2018.
 */

public class NoticeAdapter extends BaseAdapter
{
        List<NoticeModel>noticeModels;
        Context context;
        View view;
        Activity activity;
        NeedSomeMethod someMethod;

        public NoticeAdapter(Context context,List<NoticeModel>noticeModels)
        {
                this.context = context;
                activity = (Activity)context;
                this.noticeModels = noticeModels;
                someMethod = new NeedSomeMethod(context);
        }

        @Override
        public int getCount() {
                return noticeModels.size();
        }

        @Override
        public Object getItem(int position) {
                return noticeModels.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.notice_model, parent, false);
                TextView txtUserName,txtDate,txtTitle,txtId;
                ReadMoreTextView moreTextView;
                Button bRemove;

                txtUserName = view.findViewById(R.id.txtUserName);
                txtDate = view.findViewById(R.id.txtDate);
                txtTitle = view.findViewById(R.id.txtTitle);
                txtId = view.findViewById(R.id.txtId);

                moreTextView = view.findViewById(R.id.txtNotice);
                bRemove = view.findViewById(R.id.bRemove);

                txtUserName.setText(noticeModels.get(position).getUserName());
                txtDate.setText(noticeModels.get(position).getDate());
                txtTitle.setText(noticeModels.get(position).getTitle());
                txtId.setText("#P"+noticeModels.get(position).getId());
                moreTextView.setText(noticeModels.get(position).getNotice());

                bRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                removeNotice(noticeModels.get(position).getId());
                        }
                });

                return view;
        }


        private void removeNotice(final String noticeId)
        {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.dialog_warning,null);

                builder.setView(view);
                builder.setCancelable(false);

                final Button bYes,bNo;
                TextView txtWarning;

                bYes = view.findViewById(R.id.bYes);
                bNo = view.findViewById(R.id.bNo);
                txtWarning = view.findViewById(R.id.text);

                txtWarning.setText("Want to remove this notice?");

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                bYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                String id = noticeId.substring(0,noticeId.length());

                                String file = "http://192.168.56.1/removeNotice.php";
                                String post;

                                try {
                                        post = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");

                                        InfoBackgroundTask backgroundTask = new InfoBackgroundTask(context);
                                        backgroundTask.setOnResultListener(onAsyncTaskInterface);
                                        backgroundTask.execute(file,post);

                                } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                }
                                alertDialog.dismiss();
                        }
                });

                bNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                alertDialog.dismiss();
                        }
                });
        }


        private OnAsyncTaskInterface onAsyncTaskInterface = new OnAsyncTaskInterface() {
                @Override
                public void onResultSuccess(final String message) {
                        activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                        if(message.equals("success"))
                                        {
                                                context.startActivity(new Intent(context,NoticeBoard.class));
                                                activity.finish();
                                        }
                                }
                        });
                }
        };
}
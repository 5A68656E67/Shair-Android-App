/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  18641 java smart phone development - final project - Shair
 *
 *  Name: Sen Yue (seny)
 *        Zheng Lei (zlei)
 *
 *  class name: ShareFragment
 *
 *  class properties:
 *  addShairButton: TextView
 *  mGridViewPost: StaggeredGridView
 *  mGridViewShare: StaggeredGridView
 *  mGridViewBorrow: StaggeredGridView
 *  gridAdapterBorrow: GridAdapterBorrow
 *  gridAdapterPost: gridAdapterPost
 *  gridAdapterShare: gridAdapterShare
 *  buildAccount: BuildAccount
 *  choosePost: TextView
 *  chooseshare: TextView
 *  chooseborrow: TextView
 *  refresh: TextView
 *  refreshTask: RefreshTask
 *  sqlDataBase: SQLDataBase
 *
 *  class methods:
 *  onCreate(Bundle savedInstanceState):void
 *  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState):View
 *  addChooseListener(): void
 *  addRefreshListener(): void
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package com.example.ethan.shairversion1application.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.etsy.android.grid.StaggeredGridView;
import com.example.ethan.shairversion1application.R;
import com.example.ethan.shairversion1application.adapter.GridAdapterBorrow;
import com.example.ethan.shairversion1application.adapter.GridAdapterPost;
import com.example.ethan.shairversion1application.adapter.GridAdapterShare;
import com.example.ethan.shairversion1application.cruditem.BuildAccount;
import com.example.ethan.shairversion1application.database.SQLDataBase;
import com.example.ethan.shairversion1application.dialog.AddThingsToShareDialog;
import com.example.ethan.shairversion1application.entities.Account;
import com.example.ethan.shairversion1application.entities.Item;
import com.example.ethan.shairversion1application.socket.DefaultSocketClient;
import com.example.ethan.shairversion1application.socket.QueryType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ObjectInputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection", "AccessStaticViaInstance", "unused"})
public class ShareFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView addShairButton;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private StaggeredGridView mGridViewPost;
    private StaggeredGridView mGridViewShare;
    private StaggeredGridView mGridViewBorrow;
    private GridAdapterBorrow gridAdapterBorrow;
    private GridAdapterPost gridAdapterPost;
    private GridAdapterShare gridAdapterShare;
    private BuildAccount buildAccount;
    private TextView choosePost;
    private TextView chooseshare;
    private TextView chooseborrow;
    private TextView refresh;
    private RefreshTask refreshTask;
    private SQLDataBase sqlDataBase;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareFragment.
     */

    @SuppressWarnings("unused")
    public static ShareFragment newInstance(String param1, String param2) {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        buildAccount = new BuildAccount();

        View oldview = getActivity().getCurrentFocus();
        if (oldview != null) {
            Log.e("not null bitch", "shit");
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(oldview.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        View view = inflater.inflate(R.layout.ui_sharefragment, container, false);
        sqlDataBase = new SQLDataBase(getActivity());
        refresh = (TextView) view.findViewById(R.id.refresh);
        addShairButton = (TextView) view.findViewById(R.id.add_shair_button);

        addShairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                AddThingsToShareDialog attsDialog = new AddThingsToShareDialog();
                attsDialog.show(fm,"add_things_to_share");
            }
        });
        choosePost = (TextView) view.findViewById(R.id.choose_post);
        chooseshare = (TextView) view.findViewById(R.id.choose_share);
        chooseborrow = (TextView) view.findViewById(R.id.choose_borrow);


        mGridViewPost = (StaggeredGridView) view.findViewById(R.id.grid_view_post);
        mGridViewShare = (StaggeredGridView) view.findViewById(R.id.grid_view_share);
        mGridViewBorrow = (StaggeredGridView) view.findViewById(R.id.grid_view_borrow);

        gridAdapterPost = new GridAdapterPost(getActivity(), buildAccount.getAccount().getUser().getPostedItemArrayList());
        gridAdapterShare = new GridAdapterShare(getActivity(), buildAccount.getAccount().getUser().getSharedItemArrayList());
        gridAdapterBorrow = new GridAdapterBorrow(getActivity(), buildAccount.getAccount().getUser().getBorrowedItemArrayList());

        mGridViewPost.setAdapter(gridAdapterPost);
        mGridViewShare.setAdapter(gridAdapterShare);
        mGridViewBorrow.setAdapter(gridAdapterBorrow);
        choosePost.setTextColor(Color.parseColor("#FFFF3c3c"));
        chooseshare.setTextColor(Color.parseColor("#40000000"));
        chooseborrow.setTextColor(Color.parseColor("#40000000"));
        mGridViewPost.setVisibility(View.VISIBLE);
        mGridViewShare.setVisibility(View.GONE);
        mGridViewBorrow.setVisibility(View.GONE);

        mGridViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ConnectivityManager cm =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (!isConnected) {
                    LayoutInflater inflater = getLayoutInflater(null);
                    View toastView = inflater.inflate(R.layout.offline_toast,
                            (ViewGroup) getActivity().findViewById(R.id.toast_offline));
                    Toast toast = new Toast(getActivity());
                    toast.setView(toastView);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }

                Item item = (Item) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putInt("type", 3);
                Intent intent = new Intent(getActivity(), PostedItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mGridViewShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ConnectivityManager cm =
                        (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected){
                    LayoutInflater inflater = getLayoutInflater(null);
                    View toastView = inflater.inflate(R.layout.offline_toast,
                            (ViewGroup) getActivity().findViewById(R.id.toast_offline));
                    Toast toast = new Toast(getActivity());
                    toast.setView(toastView);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }

                Item item = (Item) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putInt("type", 3);
                Intent intent = new Intent(getActivity(), SharedItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mGridViewBorrow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                ConnectivityManager cm =
                        (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(!isConnected){
                    LayoutInflater inflater = getLayoutInflater(null);
                    View toastView = inflater.inflate(R.layout.offline_toast,
                            (ViewGroup) getActivity().findViewById(R.id.toast_offline));
                    Toast toast = new Toast(getActivity());
                    toast.setView(toastView);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    return;
                }

                Item item = (Item) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                bundle.putInt("type", 3);
                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        addChooseListener();
        addRefreshListener();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        @SuppressWarnings("UnnecessaryInterfaceModifier")
        public void onFragmentInteraction(Uri uri);
    }

    public void addChooseListener() {
        choosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePost.setTextColor(Color.parseColor("#FFFF3c3c"));
                chooseshare.setTextColor(Color.parseColor("#40000000"));
                chooseborrow.setTextColor(Color.parseColor("#40000000"));
                mGridViewPost.setVisibility(View.VISIBLE);
                mGridViewShare.setVisibility(View.GONE);
                mGridViewBorrow.setVisibility(View.GONE);
            }
        });
        chooseshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePost.setTextColor(Color.parseColor("#40000000"));
                chooseshare.setTextColor(Color.parseColor("#FFFF3c3c"));
                chooseborrow.setTextColor(Color.parseColor("#40000000"));
                mGridViewPost.setVisibility(View.GONE);
                mGridViewShare.setVisibility(View.VISIBLE);
                mGridViewBorrow.setVisibility(View.GONE);
            }
        });
        chooseborrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePost.setTextColor(Color.parseColor("#40000000"));
                chooseshare.setTextColor(Color.parseColor("#40000000"));
                chooseborrow.setTextColor(Color.parseColor("#FFFF3c3c"));
                mGridViewPost.setVisibility(View.GONE);
                mGridViewShare.setVisibility(View.GONE);
                mGridViewBorrow.setVisibility(View.VISIBLE);
            }
        });
    }

    public void addRefreshListener() {
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account tempAccount = sqlDataBase.getAcount().get(0);
                System.out.println("account name from database: " + tempAccount.getAccountName());
                System.out.println("account password from database: " + tempAccount.getAccountPassword());
                refreshTask = new RefreshTask(tempAccount.getAccountName(),tempAccount.getAccountPassword());
                refreshTask.execute((Void) null);
                gridAdapterPost = new GridAdapterPost(getActivity(), buildAccount.getAccount().getUser().getPostedItemArrayList());
                gridAdapterShare = new GridAdapterShare(getActivity(), buildAccount.getAccount().getUser().getSharedItemArrayList());
                gridAdapterBorrow = new GridAdapterBorrow(getActivity(), buildAccount.getAccount().getUser().getBorrowedItemArrayList());

                mGridViewPost.setAdapter(gridAdapterPost);
                mGridViewShare.setAdapter(gridAdapterShare);
                mGridViewBorrow.setAdapter(gridAdapterBorrow);
            }
        });
    }





    public class RefreshTask extends AsyncTask<Void, Void, Boolean> {

        private final String taskkey;
        private final String taskvalue;
        private String jsonStr;
        private int i = -1;

        RefreshTask(String key, String value) {
            this.taskkey = key;
            this.taskvalue = value;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                JsonObject keyvaluePair = new JsonObject();
                keyvaluePair.addProperty("key",taskkey);
                keyvaluePair.addProperty("value", taskvalue);
                DefaultSocketClient client = new DefaultSocketClient(QueryType.LOGIN_ACCOUNT,keyvaluePair);
                client.start();
                ObjectInputStream ois = client.getInputStream();
                i = (Integer) ois.readObject();
                if(i == 1){
                    jsonStr = (String) ois.readObject();
                    new BuildAccount().build(new JsonParser().parse(jsonStr).getAsJsonObject());
                }
                client.setSuccess(true);
            }catch (Exception e){
                e.printStackTrace();
            }
            //noinspection RedundantIfStatement
            if(i == 1){
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

    }



}
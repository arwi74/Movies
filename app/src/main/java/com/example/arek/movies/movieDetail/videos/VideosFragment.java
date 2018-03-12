package com.example.arek.movies.movieDetail.videos;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arek.movies.MoviesApp;
import com.example.arek.movies.R;
import com.example.arek.movies.adapter.VideosAdapter;
import com.example.arek.movies.databinding.FragmentVideosBinding;
import com.example.arek.movies.model.Video;
import com.example.arek.movies.repository.VideosRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment implements
        VideosContract.View,
        VideosAdapter.VideosAdapterOnClickListener{
    private static final String ARG_MOVIE_ID = "param1";
    private  static final String LOG_TAG = VideosFragment.class.getSimpleName();

    private long mMovieId;

    private OnFragmentInteractionListener mListener;

    private FragmentVideosBinding mBinding;
    private RecyclerView mRecyclerView;
    private VideosAdapter mVideoAdapter;
    @Inject
    public VideosRepository mVideoRepository;
    private VideosContract.Presenter mPresenter;

    public VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment VideosFragment.
     */

    public static VideosFragment newInstance(long param1) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovieId = getArguments().getLong(ARG_MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_videos, container,false);
        setRecyclerView();
        mVideoAdapter = new VideosAdapter(this, getActivity());
        mRecyclerView.setAdapter(mVideoAdapter);
        ((MoviesApp) getActivity().getApplication()).getRepositoryComponent().inject(this);

//        LinearSnapHelper helper = new LinearSnapHelper();
//        helper.attachToRecyclerView(mRecyclerView);

        mPresenter = new VideosPresenter(mVideoRepository);
        mPresenter.takeView(this);
        mPresenter.loadVideos(mMovieId);
        Log.d(LOG_TAG,"videos create");
        return mBinding.getRoot();
    }

    @Override
    public void showVideos(List<Video> videos){
        for (Video video: videos){
            Log.d(LOG_TAG,video.getName());
        }
        mVideoAdapter.addVideos(videos);
    }

    @Override
    public void showProgressBar(){
        mBinding.videosProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar(){
        mBinding.videosProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoVideosImage(){
        mBinding.videosNoVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoVideosImage() {
        mBinding.videosNoVideos.setVisibility(View.GONE);
    }

    private void setRecyclerView(){
        mRecyclerView = mBinding.videosRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
      //  mRecyclerView.setNestedScrollingEnabled(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mPresenter.dropView();
    }

    @Override
    public void onVideoClick(String videoKey) {
        mPresenter.openVideo(getActivity(), videoKey);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

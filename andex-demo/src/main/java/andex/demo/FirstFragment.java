package andex.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.widget.TextView;

import andex.core.BaseFlowV4Fragment;

public class FirstFragment extends BaseFlowV4Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String title = (String) getArgFromPrevious("title");
        String content = (String) getArgFromPrevious("content");

        this.getActivity().setTitle(title);
        ((TextView)view.findViewById(R.id.textview_first)).setText(content);

        view.findViewById(R.id.button_first).setOnClickListener(v -> {
                    ((FlowDemoActivity) getActivity()).toSecond();
                }
        );
    }
}

package com.xlntsmmr.xlnt_timeline;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.xlntsmmr.xlnt_timeline.Adapter.CalendarAdapter;
import com.xlntsmmr.xlnt_timeline.Adapter.CategoryListMoveAdapter;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    String TAG = "ItemMoveCallback";

    private final CategoryListMoveAdapter mAdapter;

    public ItemMoveCallback(CategoryListMoveAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // 위, 아래로 드래그 가능
        int swipeFlags = 0; // 스와이프 동작은 사용하지 않음
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true; // true를 반환하여 아이템이 이동했다고 알립니다.
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 스와이프 동작이 필요한 경우 여기에 추가합니다.
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true; // 롱 프레스로 아이템을 드래그할 수 있도록 설정
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

    }
}

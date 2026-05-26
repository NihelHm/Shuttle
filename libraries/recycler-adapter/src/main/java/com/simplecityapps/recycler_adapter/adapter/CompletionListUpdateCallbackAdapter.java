package com.simplecityapps.recycler_adapter.adapter;

public class CompletionListUpdateCallbackAdapter implements CompletionListUpdateCallback {

    @Override
    public void onInserted(int position, int count) {
        // Intentionally left empty.
    }

    @Override
    public void onRemoved(int position, int count) {
        // Intentionally left empty.
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        // Intentionally left empty.
    }

    @Override
    public void onChanged(int position, int count, Object payload) {
        // Intentionally left empty.
    }

    public void onComplete() {
        // Intentionally left empty.
    }
}

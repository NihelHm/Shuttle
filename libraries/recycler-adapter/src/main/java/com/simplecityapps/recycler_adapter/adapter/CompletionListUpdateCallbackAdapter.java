package com.simplecityapps.recycler_adapter.adapter;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"})
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

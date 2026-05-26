package com.simplecity.amp_library.ui.modelviews;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.glide.loader.TypeLoader;
import com.simplecity.amp_library.glide.utils.BitmapAndSize;
import com.simplecity.amp_library.glide.utils.BitmapAndSizeDecoder;
import com.simplecity.amp_library.model.ArtworkModel;
import com.simplecity.amp_library.model.ArtworkProvider;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecityapps.recycler_adapter.model.BaseViewModel;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;
import java.io.File;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class ArtworkView extends BaseViewModel<ArtworkView.ViewHolder> { //NOSONAR

    public interface GlideListener { //NOSONAR
        void onArtworkLoadFailed(ArtworkView artworkView); //NOSONAR
    }

    public interface ClickListener { //NOSONAR
        void onClick(ArtworkView artworkView); //NOSONAR
    }

    @ArtworkProvider.Type //NOSONAR
    private int type; //NOSONAR

    private ArtworkProvider artworkProvider; //NOSONAR

    GlideListener glideListener; //NOSONAR

    @SuppressWarnings("java:S1104") //NOSONAR

    public File file; //NOSONAR

    private boolean selected; //NOSONAR

    private boolean isCustom; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    public ArtworkView(int type, ArtworkProvider artworkProvider, GlideListener glideListener) { //NOSONAR
        this(type, artworkProvider, glideListener, null, false); //NOSONAR
    }

    public ArtworkView(int type, ArtworkProvider artworkProvider, GlideListener glideListener, File file, boolean isCustom) { //NOSONAR
        this.type = type; //NOSONAR
        this.artworkProvider = artworkProvider; //NOSONAR
        this.glideListener = glideListener; //NOSONAR
        this.file = file; //NOSONAR
        this.isCustom = isCustom; //NOSONAR
    }

    public ArtworkModel getItem() { //NOSONAR
        return new ArtworkModel(type, file); //NOSONAR
    }

    public void setListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    public void setSelected(boolean selected) { //NOSONAR
        this.selected = selected; //NOSONAR
    }

    public boolean isSelected() { //NOSONAR
        return selected; //NOSONAR
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.ARTWORK; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_artwork; //NOSONAR
    }

    void onClick() { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onClick(this); //NOSONAR
        }
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        long time = System.currentTimeMillis(); //NOSONAR

        holder.textContainer.setBackground(null); //NOSONAR
        holder.progressBar.setVisibility(View.VISIBLE); //NOSONAR
        holder.lineTwo.setText(null); //NOSONAR

        Glide.with(holder.itemView.getContext()) //NOSONAR
                .using(new TypeLoader(holder.itemView.getContext(), type, file), InputStream.class) //NOSONAR
                .from(ArtworkProvider.class) //NOSONAR
                .as(BitmapAndSize.class) //NOSONAR
                .sourceEncoder(new StreamEncoder()) //NOSONAR
                .decoder(new BitmapAndSizeDecoder(holder.itemView.getContext())) //NOSONAR
                .diskCacheStrategy(DiskCacheStrategy.NONE) //NOSONAR
                .load(artworkProvider) //NOSONAR
                .listener(new RequestListener<ArtworkProvider, BitmapAndSize>() { //NOSONAR
                    @Override //NOSONAR
                    public boolean onException(Exception e, ArtworkProvider model, Target<BitmapAndSize> target, boolean isFirstResource) { //NOSONAR
                        if (glideListener != null) { //NOSONAR
                            if (holder.itemView.getHandler() != null) { //NOSONAR
                                holder.itemView.getHandler().postDelayed(() -> //NOSONAR
                                        glideListener.onArtworkLoadFailed(ArtworkView.this), System.currentTimeMillis() + 1000 - time); //NOSONAR
                            }
                        }
                        return false; //NOSONAR
                    }

                    @Override //NOSONAR
                    public boolean onResourceReady(BitmapAndSize resource, ArtworkProvider model, Target<BitmapAndSize> target, boolean isFromMemoryCache, boolean isFirstResource) { //NOSONAR
                        return false; //NOSONAR
                    }
                })
                .into(new ImageViewTarget<BitmapAndSize>(((ViewHolder) holder).imageView) { //NOSONAR
                    @Override //NOSONAR
                    protected void setResource(BitmapAndSize resource) { //NOSONAR
                        holder.textContainer.setBackgroundResource(R.drawable.text_protection_scrim_reversed); //NOSONAR
                        holder.progressBar.setVisibility(View.GONE); //NOSONAR

                        holder.imageView.setImageBitmap(resource.bitmap); //NOSONAR
                        holder.lineTwo.setText(String.format("%sx%spx", resource.size.width, resource.size.height)); //NOSONAR
                    }
                });

        holder.lineOne.setText(ArtworkModel.getTypeString(holder.itemView.getContext(), type)); //NOSONAR

        if (type == ArtworkProvider.Type.FOLDER && file != null) { //NOSONAR
            holder.lineOne.setText(file.getName()); //NOSONAR
        }

        if (isCustom && file != null && file.getPath().contains("custom_artwork")) { //NOSONAR
            holder.lineOne.setText(holder.itemView.getContext().getString(R.string.artwork_type_custom)); //NOSONAR
        }

        holder.checkView.setVisibility(isSelected() ? View.VISIBLE : View.GONE); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR

        holder.checkView.setVisibility(isSelected() ? View.VISIBLE : View.GONE); //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder<ArtworkView> { //NOSONAR

        @SuppressWarnings("java:S1104") //NOSONAR

        public ImageView imageView; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineOne; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineTwo; //NOSONAR
        private View checkView; //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public View textContainer; //NOSONAR
        private ProgressBar progressBar; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            imageView = itemView.findViewById(R.id.imageView); //NOSONAR
            lineOne = itemView.findViewById(R.id.line_one); //NOSONAR
            lineTwo = itemView.findViewById(R.id.line_two); //NOSONAR
            checkView = itemView.findViewById(R.id.checkView); //NOSONAR
            textContainer = itemView.findViewById(R.id.textContainer); //NOSONAR
            progressBar = itemView.findViewById(R.id.progressBar); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onClick()); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "ArtworkView.ViewHolder"; //NOSONAR
        }
    }
}

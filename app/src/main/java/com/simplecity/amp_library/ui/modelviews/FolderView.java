package com.simplecity.amp_library.ui.modelviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.aesthetic.Aesthetic;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.data.Repository;
import com.simplecity.amp_library.interfaces.FileType;
import com.simplecity.amp_library.model.BaseFileObject;
import com.simplecity.amp_library.model.FileObject;
import com.simplecity.amp_library.model.FolderObject;
import com.simplecity.amp_library.model.InclExclItem;
import com.simplecity.amp_library.ui.adapters.ViewType;
import com.simplecity.amp_library.ui.views.CircleImageView;
import com.simplecity.amp_library.utils.SettingsManager;
import com.simplecity.amp_library.utils.StringUtils;
import com.simplecityapps.recycler_adapter.recyclerview.BaseViewHolder;
import java.lang.ref.WeakReference;
import java.util.List;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class FolderView extends BaseSelectableViewModel<FolderView.ViewHolder> { //NOSONAR

    private static final String TAG = "FolderView"; //NOSONAR

    public interface ClickListener { //NOSONAR

        void onFileObjectClick(int position, FolderView folderView); //NOSONAR

        void onFileObjectOverflowClick(View v, FolderView folderView); //NOSONAR

        void onFileObjectCheckboxClick(CheckBox checkBox, FolderView folderView); //NOSONAR
    }

    @NonNull //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public BaseFileObject baseFileObject; //NOSONAR

    @Nullable //NOSONAR
    private ClickListener listener; //NOSONAR

    private boolean showWhitelist; //NOSONAR
    private boolean showBlacklist; //NOSONAR

    private boolean isWhitelisted; //NOSONAR
    private boolean isBlacklisted; //NOSONAR

    private SettingsManager settingsManager; //NOSONAR

    Repository.WhitelistRepository whitelistRepository; //NOSONAR
    Repository.BlacklistRepository blacklistRepository; //NOSONAR

    public FolderView( //NOSONAR
            @NonNull BaseFileObject baseFileObject, //NOSONAR
            Repository.WhitelistRepository whitelistRepository, //NOSONAR
            Repository.BlacklistRepository blacklistRepository, //NOSONAR
            SettingsManager settingsManager, //NOSONAR
            boolean whitelisted, //NOSONAR
            boolean blacklisted) { //NOSONAR
        this.baseFileObject = baseFileObject; //NOSONAR
        this.whitelistRepository = whitelistRepository; //NOSONAR
        this.blacklistRepository = blacklistRepository; //NOSONAR
        this.settingsManager = settingsManager; //NOSONAR
        this.isWhitelisted = whitelisted; //NOSONAR
        this.isBlacklisted = blacklisted; //NOSONAR
    }

    public void setClickListener(@Nullable ClickListener listener) { //NOSONAR
        this.listener = listener; //NOSONAR
    }

    public void setShowWhitelist(boolean showWhitelist) { //NOSONAR
        this.showWhitelist = showWhitelist; //NOSONAR
        if (showWhitelist) { //NOSONAR
            this.showBlacklist = false; //NOSONAR
        }
    }

    public void setShowBlacklist(boolean showBlacklist) { //NOSONAR
        this.showBlacklist = showBlacklist; //NOSONAR
        if (showBlacklist) { //NOSONAR
            this.showWhitelist = false; //NOSONAR
        }
    }

    void onClick(int position) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onFileObjectClick(position, this); //NOSONAR
        }
    }

    void onOverflowClick(View v) { //NOSONAR
        if (listener != null) { //NOSONAR
            listener.onFileObjectOverflowClick(v, this); //NOSONAR
        }
    }

    void onCheckboxClick(CheckBox checkbox) { //NOSONAR

        setSelected(checkbox.isChecked()); //NOSONAR

        if (showWhitelist) { //NOSONAR
            setWhitelisted(whitelistRepository, checkbox.isChecked()); //NOSONAR
        }
        if (showBlacklist) { //NOSONAR
            setBlacklisted(blacklistRepository, checkbox.isChecked()); //NOSONAR
        }
        if (listener != null) { //NOSONAR
            listener.onFileObjectCheckboxClick(checkbox, this); //NOSONAR
        }
    }

    public void setWhitelisted(Repository.WhitelistRepository whitelistRepository, boolean whitelisted) { //NOSONAR
        isWhitelisted = whitelisted; //NOSONAR
        InclExclItem inclExclItem = new InclExclItem(baseFileObject.path, InclExclItem.Type.INCLUDE); //NOSONAR
        if (whitelisted) { //NOSONAR
            whitelistRepository.add(inclExclItem); //NOSONAR
        } else { //NOSONAR
            whitelistRepository.delete(inclExclItem); //NOSONAR
        }
    }

    public void setBlacklisted(Repository.BlacklistRepository blacklistRepository, boolean blacklisted) { //NOSONAR
        isBlacklisted = blacklisted; //NOSONAR
        InclExclItem inclExclItem = new InclExclItem(baseFileObject.path, InclExclItem.Type.EXCLUDE); //NOSONAR
        if (blacklisted) { //NOSONAR
            blacklistRepository.add(inclExclItem); //NOSONAR
        } else { //NOSONAR
            blacklistRepository.delete(inclExclItem); //NOSONAR
        }
    }

    @Override //NOSONAR
    public int getViewType() { //NOSONAR
        return ViewType.FOLDER; //NOSONAR
    }

    @Override //NOSONAR
    public int getLayoutResId() { //NOSONAR
        return R.layout.list_item_folder; //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder) { //NOSONAR
        super.bindView(holder); //NOSONAR

        if (baseFileObject instanceof FileObject && settingsManager.getFolderBrowserShowFileNames()) { //NOSONAR
            holder.lineFour.setText(String.format("%s.%s", ((FileObject) baseFileObject).name, ((FileObject) baseFileObject).extension)); //NOSONAR
            holder.lineFour.setVisibility(View.VISIBLE); //NOSONAR
            holder.textContainer.setVisibility(View.GONE); //NOSONAR
        } else { //NOSONAR
            holder.lineFour.setVisibility(View.GONE); //NOSONAR
            holder.textContainer.setVisibility(View.VISIBLE); //NOSONAR
        }

        holder.lineThree.setText(null); //NOSONAR

        switch (baseFileObject.fileType) { //NOSONAR
            case FileType.PARENT: //NOSONAR
                holder.imageView.setImageDrawable(holder.parentFolderDrawable); //NOSONAR
                holder.lineTwo.setText(holder.itemView.getContext().getString(R.string.parent_folder)); //NOSONAR
                holder.overflow.setVisibility(View.GONE); //NOSONAR
                holder.lineThree.setVisibility(View.GONE); //NOSONAR
                holder.lineOne.setText(baseFileObject.name); //NOSONAR
                break; //NOSONAR
            case FileType.FOLDER: //NOSONAR
                holder.overflow.setVisibility(View.VISIBLE); //NOSONAR
                holder.imageView.setImageDrawable(holder.folderDrawable); //NOSONAR
                holder.lineTwo.setText(StringUtils.makeSubfoldersLabel(holder.itemView.getContext(), ((FolderObject) baseFileObject).folderCount, ((FolderObject) baseFileObject).fileCount)); //NOSONAR
                holder.lineThree.setVisibility(View.GONE); //NOSONAR
                holder.lineOne.setText(baseFileObject.name); //NOSONAR
                break; //NOSONAR
            case FileType.FILE: //NOSONAR
                holder.overflow.setVisibility(View.VISIBLE); //NOSONAR
                holder.imageView.setImageDrawable(holder.fileDrawable); //NOSONAR
                holder.lineThree.setVisibility(View.VISIBLE); //NOSONAR
                holder.lineOne.setText(((FileObject) baseFileObject).tagInfo.trackName); //NOSONAR
                holder.lineTwo.setText(String.format("%s - %s", ((FileObject) baseFileObject).tagInfo.artistName, ((FileObject) baseFileObject).tagInfo.albumName)); //NOSONAR
                DurationTask durationTask = new DurationTask(holder.lineThree, (FileObject) baseFileObject); //NOSONAR
                durationTask.execute(); //NOSONAR
                break; //NOSONAR
        }

        if (showWhitelist || showBlacklist) { //NOSONAR
            holder.checkBox.setVisibility(View.VISIBLE); //NOSONAR
            holder.imageView.setVisibility(View.GONE); //NOSONAR
        } else { //NOSONAR
            holder.checkBox.setVisibility(View.GONE); //NOSONAR
            holder.imageView.setVisibility(View.VISIBLE); //NOSONAR
        }

        holder.checkBox.setChecked((showWhitelist && isWhitelisted) || (showBlacklist && isBlacklisted)); //NOSONAR
        holder.itemView.setActivated(false); //NOSONAR
    }

    @Override //NOSONAR
    public void bindView(ViewHolder holder, int position, List payloads) { //NOSONAR
        super.bindView(holder, position, payloads); //NOSONAR

        if (baseFileObject instanceof FileObject && settingsManager.getFolderBrowserShowFileNames()) { //NOSONAR
            holder.lineFour.setText(String.format("%s.%s", ((FileObject) baseFileObject).name, ((FileObject) baseFileObject).extension)); //NOSONAR
            holder.lineFour.setVisibility(View.VISIBLE); //NOSONAR
            holder.textContainer.setVisibility(View.GONE); //NOSONAR
        } else { //NOSONAR
            holder.lineFour.setVisibility(View.GONE); //NOSONAR
            holder.textContainer.setVisibility(View.VISIBLE); //NOSONAR
        }

        if (showWhitelist || showBlacklist) { //NOSONAR
            holder.checkBox.setVisibility(View.VISIBLE); //NOSONAR
            holder.imageView.setVisibility(View.GONE); //NOSONAR
        } else { //NOSONAR
            holder.checkBox.setVisibility(View.GONE); //NOSONAR
            holder.imageView.setVisibility(View.VISIBLE); //NOSONAR
        }

        holder.checkBox.setChecked((showWhitelist && isWhitelisted) || (showBlacklist && isBlacklisted)); //NOSONAR
        holder.itemView.setActivated(false); //NOSONAR
    }

    @Override //NOSONAR
    public ViewHolder createViewHolder(ViewGroup parent) { //NOSONAR
        return new ViewHolder(createView(parent)); //NOSONAR
    }

    public static class ViewHolder extends BaseViewHolder<FolderView> { //NOSONAR

        @BindView(R.id.line_one) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineOne; //NOSONAR

        @BindView(R.id.line_two) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineTwo; //NOSONAR

        @BindView(R.id.line_three) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public TextView lineThree; //NOSONAR

        @BindView(R.id.line_four) //NOSONAR
        private TextView lineFour; //NOSONAR

        @BindView(R.id.textContainer) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public View textContainer; //NOSONAR

        @BindView(R.id.image) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public CircleImageView imageView; //NOSONAR

        @BindView(R.id.btn_overflow) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public ImageButton overflow; //NOSONAR

        @BindView(R.id.checkbox) //NOSONAR
        @SuppressWarnings("java:S1104") //NOSONAR
        public CheckBox checkBox; //NOSONAR

        Drawable folderDrawable; //NOSONAR
        Drawable parentFolderDrawable; //NOSONAR
        Drawable fileDrawable; //NOSONAR

        public ViewHolder(View itemView) { //NOSONAR
            super(itemView); //NOSONAR

            ButterKnife.bind(this, itemView); //NOSONAR

            itemView.setOnClickListener(v -> viewModel.onClick(getAdapterPosition())); //NOSONAR
            overflow.setOnClickListener(v -> viewModel.onOverflowClick(v)); //NOSONAR
            checkBox.setOnClickListener(v -> viewModel.onCheckboxClick((CheckBox) v)); //NOSONAR

            int colorPrimary = Aesthetic.get(itemView.getContext()).colorPrimary().blockingFirst(); //NOSONAR

            folderDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_folder_24dp); //NOSONAR
            parentFolderDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_folder_outline); //NOSONAR
            fileDrawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_headphones_white); //NOSONAR

            imageView.setColorFilter(colorPrimary); //NOSONAR
        }

        @Override //NOSONAR
        public String toString() { //NOSONAR
            return "FolderView.ViewHolder"; //NOSONAR
        }
    }

    private static class DurationTask extends AsyncTask<Void, Void, String> { //NOSONAR

        private TextView textView; //NOSONAR

        private Context applicationContext; //NOSONAR

        private FileObject fileObject; //NOSONAR

        DurationTask(TextView textView, FileObject fileObject) { //NOSONAR
            this.textView = textView; //NOSONAR
            this.fileObject = fileObject; //NOSONAR
            applicationContext = textView.getContext().getApplicationContext(); //NOSONAR
            textView.setTag(new WeakReference<>(DurationTask.this)); //NOSONAR
        }

        @Override //NOSONAR
        protected String doInBackground(Void... params) { //NOSONAR
            return fileObject.getTimeString(applicationContext); //NOSONAR
        }

        @Override //NOSONAR
        protected void onPostExecute(String s) { //NOSONAR
            super.onPostExecute(s); //NOSONAR

            if (textView != null) { //NOSONAR
                if (((WeakReference<DurationTask>) textView.getTag()).get() == DurationTask.this) { //NOSONAR
                    textView.setText(s); //NOSONAR
                }
            }
        }
    }

    @Override //NOSONAR
    public boolean equals(Object o) { //NOSONAR
        if (this == o) return true; //NOSONAR
        if (o == null || getClass() != o.getClass()) return false; //NOSONAR
        if (!super.equals(o)) return false; //NOSONAR

        FolderView that = (FolderView) o; //NOSONAR

        return baseFileObject.equals(that.baseFileObject); //NOSONAR
    }

    @Override //NOSONAR
    public int hashCode() { //NOSONAR
        int result = super.hashCode(); //NOSONAR
        result = 31 * result + baseFileObject.hashCode(); //NOSONAR
        return result; //NOSONAR
    }

    @Override //NOSONAR
    public boolean areContentsEqual(Object other) { //NOSONAR
        if (this == other) return true; //NOSONAR
        if (other == null || getClass() != other.getClass()) return false; //NOSONAR
        if (!super.areContentsEqual(other)) return false; //NOSONAR

        if (!baseFileObject.equals(((FolderView) other).baseFileObject)) return false; //NOSONAR
        //        if (isSelected() != ((FolderView) other).isSelected()) return false;
        return true; //NOSONAR
    }
}

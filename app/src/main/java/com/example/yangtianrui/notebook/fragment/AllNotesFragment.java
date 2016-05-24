package com.example.yangtianrui.notebook.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.example.yangtianrui.notebook.R;
import com.example.yangtianrui.notebook.activity.NoteDetailActivity;
import com.example.yangtianrui.notebook.adapter.ShowNoteAdapter;
import com.example.yangtianrui.notebook.db.NoteDAO;

/**
 * Created by yangtianrui on 16-5-21.
 * 显示所有Note,使用Loader实现异步加载
 */
public class AllNotesFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private NoteDAO mNoteDAO;
    private ListView mLvNotes;
    private CursorAdapter mAdapter;
    private Cursor mCursor;
    private final static int CONTEXT_UPDATE_ORDER = 0;
    private final static int CONTEXT_DELETE_ORDER = 1;
    private View root;

    public AllNotesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteDAO = new NoteDAO(getActivity());
        // 查询所有行
        mCursor = mNoteDAO.queryNote(null, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_all_note, container, false);
        mLvNotes = (ListView) root.findViewById(R.id.id_lv_all_note);
        mAdapter = new ShowNoteAdapter(getActivity(), mCursor);
        getLoaderManager().initLoader(0, null, this);
        mLvNotes.setAdapter(mAdapter);
        mLvNotes.setOnItemClickListener(this);
        registerForContextMenu(mLvNotes);
        return root;
    }

    /**
     * 此时重启Loader机制更新数据
     */
    @Override
    public void onResume() {
        super.onResume();
        mCursor = mNoteDAO.queryNote(null, null);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCursor.close();
    }


    /**
     * 上下文菜单的回调函数
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //int position = mAdapter.getItem(info.position);
        int position = info.position; // list中的位置
        Cursor c = (Cursor) mAdapter.getItem(position); // CursorAdapter中getItem()返回特定的cursor对象
        int itemID = c.getInt(c.getColumnIndex("_id"));
        switch (item.getOrder()) {
            case CONTEXT_UPDATE_ORDER: // 更新操作
                //Toast.makeText(getActivity(),"UPDATE",Toast.LENGTH_SHORT).show();
                break;
            case CONTEXT_DELETE_ORDER: // 删除操作
                //Toast.makeText(getActivity(),"DELETE",Toast.LENGTH_SHORT).show();
                mNoteDAO.deleteNote("_id=?", new String[]{itemID + ""});
                getLoaderManager().restartLoader(0, null, this);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 创建上下文菜单
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Enter your choice:");
        menu.add(0, v.getId(), CONTEXT_UPDATE_ORDER, "UPDATE");
        menu.add(0, v.getId(), CONTEXT_DELETE_ORDER, "DELETE");
    }

    // 跳转到详情页
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = (Cursor) mAdapter.getItem(position); // CursorAdapter中getItem()返回特定的cursor对象
        int itemID = c.getInt(c.getColumnIndex("_id"));
//        Log.v("LOG", "AllNoteFragment itemID: " + itemID);
        Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.SENDED_NOTE_ID, itemID);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.parse("content://com.terry.NoteBook");

        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}

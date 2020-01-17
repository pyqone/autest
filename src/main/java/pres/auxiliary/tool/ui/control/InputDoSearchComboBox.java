package pres.auxiliary.tool.ui.control;

import java.awt.Component;  
import java.awt.event.ItemEvent;  
import java.awt.event.KeyEvent;  
import java.awt.event.KeyListener;  
import java.util.Vector;  
  
import javax.swing.ComboBoxModel;  
import javax.swing.DefaultComboBoxModel;  
import javax.swing.JComboBox;  
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;  
import javax.swing.SwingUtilities;  
import javax.swing.event.ListDataEvent;  
import javax.swing.plaf.basic.BasicComboPopup;  
import javax.swing.plaf.basic.ComboPopup;  
import javax.swing.plaf.metal.MetalComboBoxUI;

/** 
 * 自动过滤下拉框 
 * @author Sun 
 * 
 */  
@SuppressWarnings({ "serial", "rawtypes" })  
public class InputDoSearchComboBox extends JComboBox {  
      
    /** 
     * 显示用模型 
     */  
    protected static DefaultComboBoxModel showModel = new DefaultComboBoxModel();  
    /** 
     * 正在选择 
     */  
    private boolean selectingItem;  
      
    /** 
     * 创建一个 <code>JFilterComboBox</code>， 
     * 其项取自现有的 <code>ComboBoxModel</code>。 
     * 由于提供了 <code>ComboBoxModel</code>， 
     * 使用此构造方法创建的组合框不创建默认组合框模型， 
     * 这可能影响插入、移除和添加方法的行为方式。 
     * @param aModel - 提供显示的项列表的 <code>ComboBoxModel</code> 
     */  
    @SuppressWarnings("unchecked")
	public InputDoSearchComboBox(ComboBoxModel aModel) {  
        super(aModel);  
        initialize();  
    }  
   
    /** 
     * 创建包含指定数组中的元素的 <code>JFilterComboBox</code>。 
     * 默认情况下，选择数组中的第一项（因而也选择了该项的数据模型）。  
     * @param items - 要插入到组合框的对象数组 
     */  
    @SuppressWarnings("unchecked")
	public InputDoSearchComboBox(final Object items[]) {  
        super(items);  
        initialize();  
    }  
      
    /** 
     * 创建包含指定 <code>Vector</code> 中的元素的 <code>JFilterComboBox</code>。 
     * 默认情况下，选择数组中的第一项（因而也选择了该项的数据模型）。  
     * @param items - 要插入到组合框的向量数组 
     */  
    @SuppressWarnings("unchecked")
	public InputDoSearchComboBox(Vector<?> items) {  
        super(items);  
        initialize();  
    }  
      
    /** 
     * 创建具有默认数据模型的 <code>JFilterComboBox</code>。 
     * 默认的数据模型为空对象列表。使用 <code>addItem</code> 添加项。 
     * 默认情况下，选择数据模型中的第一项。 
     */  
    public InputDoSearchComboBox() {  
        super();  
        initialize();  
    }  
      
    private void initialize() {  
        showModel.addListDataListener(this);  
    }  
      
    @Override  
    public void updateUI() {  
        setUI(new MetalFilterComboBoxUI());  
        ListCellRenderer renderer = getRenderer();  
        if (renderer instanceof Component) {  
            SwingUtilities.updateComponentTreeUI((Component) renderer);  
        }  
    }  
      
    @Override  
    public Object getSelectedItem() {  
        return showModel.getSelectedItem();  
    }  
  
    @Override  
    public void setSelectedItem(Object anObject) {  
        Object oldSelection = selectedItemReminder;  
        Object objectToSelect = anObject;  
        if (oldSelection == null || !oldSelection.equals(anObject)) {  
  
            if (anObject != null && !isEditable()) {  
                boolean found = false;  
                for (int i = 0; i < showModel.getSize(); i++) {  
                    Object element = showModel.getElementAt(i);  
                    if (anObject.equals(element)) {  
                        found = true;  
                        objectToSelect = element;  
                        break;  
                    }  
                }  
                if (!found) {  
                    return;  
                }  
            }  
  
            selectingItem = true;  
            showModel.setSelectedItem(objectToSelect);  
            selectingItem = false;  
  
            if (selectedItemReminder != showModel.getSelectedItem()) {  
                selectedItemChanged();  
            }  
        }  
        fireActionEvent();  
    }  
  
    @Override  
    public void setSelectedIndex(int anIndex) {  
        int size = showModel.getSize();  
        if (anIndex == -1 || size == 0) {  
            setSelectedItem(null);  
        } else if (anIndex < -1) {  
            throw new IllegalArgumentException("setSelectedIndex: " + anIndex  
                    + " out of bounds");  
        } else if (anIndex >= size) {  
            setSelectedItem(showModel.getElementAt(size - 1));  
        } else {  
            setSelectedItem(showModel.getElementAt(anIndex));  
        }  
    }  
  
    @Override  
    public int getSelectedIndex() {  
        Object sObject = showModel.getSelectedItem();  
        int i, c;  
        Object obj;  
  
        for (i = 0, c = showModel.getSize(); i < c; i++) {  
            obj = showModel.getElementAt(i);  
            if (obj != null && obj.equals(sObject)) {
                return i;  
            }
        }  
        return -1;  
    }  
  
    @Override  
    public void contentsChanged(ListDataEvent e) {  
        Object oldSelection = selectedItemReminder;  
        Object newSelection = showModel.getSelectedItem();  
        if (oldSelection == null || !oldSelection.equals(newSelection)) {  
            selectedItemChanged();  
            if (!selectingItem) {  
                fireActionEvent();  
            }  
        }  
    }  
  
    @SuppressWarnings("unchecked")
	@Override  
    protected void selectedItemChanged() {  
        if (selectedItemReminder != null) {  
            fireItemStateChanged(new ItemEvent(this,  
                    ItemEvent.ITEM_STATE_CHANGED, selectedItemReminder,  
                    ItemEvent.DESELECTED));  
        }  
  
        selectedItemReminder = showModel.getSelectedItem();  
  
        if (selectedItemReminder != null) {  
            fireItemStateChanged(new ItemEvent(this,  
                    ItemEvent.ITEM_STATE_CHANGED, selectedItemReminder,  
                    ItemEvent.SELECTED));  
        }  
    }  
  
    @Override  
    public void intervalAdded(ListDataEvent e) {  
        if (selectedItemReminder != showModel.getSelectedItem()) {  
            selectedItemChanged();  
        }  
    }  
  
    @Override  
    public void setEditable(boolean aFlag) {  
        super.setEditable(true);  
    }  
  
    /** 
     * 返回显示用模型 
     * @return 
     */  
    public DefaultComboBoxModel getShowModel() {  
        return showModel;  
    }  
      
    /** 
     * Metal L&F 风格的 UI 类 
     * @author Sun 
     * 
     */  
    class MetalFilterComboBoxUI extends MetalComboBoxUI {  
          
        /** 
         * 编辑区事件监听器 
         */  
        protected EditorListener editorListener;  
        /** 
         * 该 UI 类负责绘制的控件 
         */  
        protected InputDoSearchComboBox filterComboBox;  
  
        @Override  
        public void installUI(JComponent c) {  
            filterComboBox = (InputDoSearchComboBox) c;  
            filterComboBox.setEditable(true);  
            super.installUI(c);  
        }  
  
        @Override  
        public void configureEditor() {  
            super.configureEditor();  
            editor.addKeyListener(getEditorListener());  
        }  
          
        @Override  
        public void unconfigureEditor() {  
            super.unconfigureEditor();  
            if (editorListener != null) {  
                editor.removeKeyListener(editorListener);  
                editorListener = null;  
            }  
        }  
          
        @Override  
        protected ComboPopup createPopup() {  
            return new FilterComboPopup(filterComboBox);  
        }  
          
        /** 
         * 初始化并返回编辑区事件监听器 
         * @return 
         */  
        protected EditorListener getEditorListener() {  
            if (editorListener == null) {  
                editorListener = new EditorListener();  
            }  
            return editorListener;  
        }  
  
        /** 
         * 按关键字进行查询，该方法中，可以自行加入各种查询算法 
         */  
        @SuppressWarnings("unchecked")
		protected void findMatchs() {  
            ComboBoxModel model = filterComboBox.getModel();  
            DefaultComboBoxModel showModel = filterComboBox.getShowModel();  
            showModel.removeAllElements();  
            for (int i = 0; i < model.getSize(); i++) {  
                String name = model.getElementAt(i).toString();  
                if (name.indexOf(getEditorText()) >= 0) {  
                    showModel.addElement(model.getElementAt(i));  
                }  
            }  
            ((FilterComboPopup)popup ).repaint();  
        }  
          
        /** 
         * 返回编辑区文本 
         * @return 
         */  
        private String getEditorText() {  
            return filterComboBox.getEditor().getItem().toString();  
        }  
        
          
        /** 
         * 弹出面板类 
         * @author Sun 
         * 
         */  
        class FilterComboPopup extends BasicComboPopup {  
  
            @SuppressWarnings("unchecked")
			public FilterComboPopup(JComboBox combo) {  
                super(combo);  
            }  
              
            @SuppressWarnings("unchecked")
			@Override  
            protected JList createList() {  
                JList list = super.createList();  
                list.setModel(filterComboBox.getShowModel());  
                return list;  
            }  
              
            @Override  
            public void setVisible(boolean b) {  
                super.setVisible(b);  
                if (!b) {  
                    comboBox.getEditor().setItem(comboBox.getSelectedItem());  
                }  
            }  
              
            @Override  
            public void show() {  
                findMatchs();  
                super.show();  
            }  
  
        }  
          
        /** 
         * 编辑区事件监听器类 
         * @author Sun 
         * 
         */  
        class EditorListener implements KeyListener {  
              
            @Override  
            public void keyReleased(KeyEvent e) {
            	findMatchs();
            }  
              
            @Override  
            public void keyPressed(KeyEvent e) {
            	findMatchs();
            }  
  
            /** 
             * 键入值时调用该方法
             */
            @Override  
            public void keyTyped(KeyEvent e) {  
                findMatchs();  
            }  
        }  
  
    }
}

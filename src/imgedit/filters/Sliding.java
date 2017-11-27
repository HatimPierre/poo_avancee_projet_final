package imgedit.filters;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public interface Sliding {
	default JOptionPane thresholdOpPane(){
		JOptionPane threSelect = new JOptionPane();
		JSlider mySlider = new JSlider();
		mySlider.setMinimum(0);
		mySlider.setMaximum(100);
		mySlider.setMajorTickSpacing(5);
		mySlider.setPaintTicks(true);
		mySlider.setValue(50);

		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider slider = (JSlider) e.getSource();
				if (!slider.getValueIsAdjusting())
					threSelect.setInputValue(slider.getValue());
			}
		};

		mySlider.addChangeListener(cl);

		threSelect.setMessage(new Object[]{"Set the filter's intensity:", mySlider});
		threSelect.setMessageType(JOptionPane.QUESTION_MESSAGE);
		threSelect.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		threSelect.setInputValue(50);
		JButton cancel;
		if (UIManager.getLookAndFeel().getClass().getSimpleName().equals("GTKLookAndFeel"))
			cancel = (JButton)((JPanel)threSelect.getComponents()[3]).getComponents()[1];
		else
			cancel = (JButton)((JPanel)threSelect.getComponents()[1]).getComponents()[1];
		cancel.addActionListener(e -> threSelect.setInputValue(null));

		return threSelect;
	}
}

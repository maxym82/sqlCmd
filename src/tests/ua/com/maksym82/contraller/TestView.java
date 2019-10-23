package ua.com.maksym82.contraller;

import ua.com.maksym82.view.View;

public class TestView implements View {
    private String log = "";
    private String inputString;

    public void setInputString(String inputString) {
        if (this.inputString == null) {
            this.inputString = inputString;
        } else {
            this.inputString += "\n" + inputString;
        }
    };

    @Override
    public String input(String prompt) {
        return inputString;
    }

    @Override
    public void outputln(String text) {
        log += log + "\n";

    }

    @Override
    public void output(String text) {

    }

    public String getLog () {
        return log;
    }
}

package byog.Core;

import java.io.Serializable;

public class InputParser implements Serializable {


    String input;
    private String option;
    private long seed;


    InputParser(String inputString) {

        input = inputString;
        //   parseStartGameOption(input);
    }

    private void parseStartGameOption(String inputString) {

        option = "N";
        seed = Long.parseLong(inputString.substring(1, inputString.length() - 1));
    }


    public String getOption() {
        return option;
    }

    public long getSeed() {

        return seed;
    }


}

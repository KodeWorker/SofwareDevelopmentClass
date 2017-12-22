import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import Monitoring.Patient;

public class Main {
    public static void main(String[] args){
        String filePath = args[0]; // first parsed argument is input file path
        File file  = new File(filePath);
        if (file.exists() && !file.isDirectory()){
            System.out.println("[Read Profile] from \"" + filePath + "\"\n");

            // read input file
            int LINE_LIMIT = 1000;
            String[] str = new String[LINE_LIMIT];
            try {
                FileReader reader = new FileReader(filePath);
                int character;
                int idx = 0;
                while ((character = reader.read()) != -1) {
                    if ((char)character != '\n')
                        if (str[idx] == null)
                            str[idx] = "" + (char) character;
                        else
                            str[idx] += (char)character;
                    else
                        idx += 1;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // show input file
            System.out.println("[Input File]");
            for (int i = 0; i < str.length; i++)
                if (str[i] == null)
                    break;
                else {
                    System.out.println(str[i]);
                }
            System.out.println("\n");

            // parse input file
            int monitorPeriod;
            ArrayList<Patient> patientList = new ArrayList<>();
            int patientIdx = 0;
            for (int i = 0; i < str.length; i++){

                String patientName;
                int patientPeriod;
                String deviceCategory;
                String deviceName;
                String factorDatasetFile;
                int safeRangeLowerBound;
                int safeRangeUpperBound;
                Patient patient;

                if (str[i] == null)
                    break;
                else {
                    if (i == 0) {
                        monitorPeriod = Integer.valueOf(str[i].trim());
                        System.out.println("monitor period: " + monitorPeriod);
                    }
                    else {
                            String[] parameters = str[i].split(" ");
                            if (parameters[0].equals("patient")) {
                                // new patient
                                patientName = parameters[1];
                                patientPeriod = Integer.valueOf(parameters[2].trim());
                                patient = new Patient(patientName, patientPeriod);
                                patientList.add(patient);
                                patientIdx += 1;
                            }
                            else {
                                // attach sensor device
                                deviceCategory = parameters[0];
                                deviceName = parameters[1];
                                factorDatasetFile = parameters[2];
                                safeRangeLowerBound = Integer.valueOf(parameters[3].trim());
                                safeRangeUpperBound = Integer.valueOf(parameters[4].trim());

                                patientList.get(patientIdx-1).attach(deviceCategory, deviceName, factorDatasetFile, safeRangeLowerBound, safeRangeUpperBound);
                            }
                        }
                    }
                }

            // show patient profile
            System.out.println("[SHOW PROFILE]");
            for(int i = 0; i < patientList.size(); i++){
                System.out.println("[PATIENT #" + (i+1) + "]");
                patientList.get(i).show();
            }

            // start monitoring


        }
        else{
            System.out.println("[System Ends] no such file in \"" + filePath + "\"\n");
        }
    }
}
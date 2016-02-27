package com.nerdcastle.nazmul.mealdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nazmul on 2/23/2016.
 */
public class ReportActivity extends AppCompatActivity {
    Spinner spinnerMonth;
    Spinner spinnerYear;
    TextView totalAmountTV;
    ArrayList<String>nameList;
    ArrayList<String>selfAmountList;
    ArrayList<String>officeAmountList;
    ArrayList<String>totalAmountList;
    String monthSelected;
    String deviceCurrentDateTime;
    String totalOfficePayable;
    ListView reportList;
    private static Font catFont = new Font(Font.FontFamily.COURIER, 20,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.COURIER, 16,
            Font.BOLD);
    private static Font miniBold = new Font(Font.FontFamily.COURIER, 12,
            Font.NORMAL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_report);
        initialize();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_resource, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboard:
                Intent intent = new Intent(getApplicationContext(),
                        MealSubmissionActivity.class);
                startActivity(intent);
                return true;
            case R.id.emailReport:
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("message/rfc822");
                    startActivity(emailIntent);

                return true;

            case R.id.showReport:
                try{
                    Uri readUri = Uri.parse(Environment.getExternalStorageDirectory() + "/MealManagementApp/");
                    Intent readIntent = new Intent(Intent.ACTION_VIEW);
                    readIntent.setDataAndType(readUri, "resource/folder");
                    startActivity(readIntent);
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Your file explorer not working properly!Download ES file explorer",Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void initialize() {
        totalAmountTV= (TextView) findViewById(R.id.totalAmountTV);
        reportList= (ListView) findViewById(R.id.totalReportListView);
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        spinnerMonth = (Spinner) findViewById(R.id.month);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.post(new Runnable() {
            @Override
            public void run() {
                spinnerMonth.setSelection(currentMonth-1);
            }
        });
        spinnerMonth.setSelection(adapter.getPosition(String.valueOf(currentMonth)));
        spinnerMonth.setAdapter(adapter);
        spinnerYear = (Spinner) findViewById(R.id.year);
        ArrayAdapter<CharSequence> priceadapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.post(new Runnable() {
            @Override
            public void run() {
                spinnerYear.setSelection(currentYear - 2015);
            }
        });
        spinnerYear.setAdapter(priceadapter);
    }
    public void getData(View view){
        String selectedYear=String.valueOf(spinnerYear.getSelectedItem());
        int selectedMonthAmount=spinnerMonth.getSelectedItemPosition()+1;
        monthSelected =spinnerMonth.getSelectedItem().toString();
        String selectedMonth=String.valueOf(selectedMonthAmount);
        getTotalAmount(selectedMonth, selectedYear);
        getReport(selectedMonth, selectedYear);
    }

    private void getReport(String selectedMonth, String selectedYear) {
        nameList=new ArrayList<>();
        selfAmountList=new ArrayList<>();
        officeAmountList=new ArrayList<>();
        totalAmountList=new ArrayList<>();
        String urlToGetTotalAmount="http://dotnet.nerdcastlebd.com/meal/api/meal/GetMonthlyBill?month="+selectedMonth+"&year="+selectedYear;
        JsonArrayRequest requestToGetReport=new JsonArrayRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        String name=response.getJSONObject(i).getString("EmployeeName");
                        String total=response.getJSONObject(i).getString("TotalAmount");
                        String selfAmount=response.getJSONObject(i).getString("SelfAmount");
                        String officeAmount=response.getJSONObject(i).getString("OfficeAmount");
                        nameList.add(name);
                        totalAmountList.add(total);
                        selfAmountList.add(selfAmount);
                        officeAmountList.add(officeAmount);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                AdapterForReport adapterForReport=new AdapterForReport(nameList,totalAmountList,selfAmountList,officeAmountList,getBaseContext());
                reportList.setAdapter(adapterForReport);
                //Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetReport);
    }

    private void getTotalAmount(String selectedMonth, String selectedYear) {
        String urlToGetTotalAmount="http://dotnet.nerdcastlebd.com/meal/api/meal/GetMonthlyTotalOfficeAmount?month="+selectedMonth+"&year="+selectedYear;
        StringRequest requestToGetTotalAmount=new StringRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                totalOfficePayable=response;
                totalAmountTV.setText("Total Office Payable " + response + " tk");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }
                else if(error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetTotalAmount);
    }
    public void createPdf(View view){
        generatePdf();
    }

    private void generatePdf() {
        try{
            Document document = new Document();
            String root = Environment.getExternalStorageDirectory().toString();
            deviceCurrentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            File myDir = new File(root + "/MealManagementApp");
            myDir.mkdirs();
            String fname = "Monthly Report of "+ monthSelected +"_"+ deviceCurrentDateTime +".pdf";
            File file = new File (myDir, fname);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addTitlePage(document, deviceCurrentDateTime,totalOfficePayable, monthSelected, nameList, totalAmountList, selfAmountList, officeAmountList);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Monthly Meal Report");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Nazmul Hasan");
        document.addCreator("Nazmul Hasan");
    }
    private static void addTitlePage(Document document, String deviceCurrentDateTime, String totalOfficePayable, String mnthSelected, ArrayList<String> nameList, ArrayList<String> totalAmountList, ArrayList<String> selfAmountList, ArrayList<String> officeAmountList)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Monthly Report Of " + mnthSelected, catFont));


        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        createTable(preface, nameList, totalAmountList, selfAmountList, officeAmountList);
        addEmptyLine(preface, 3);
        Paragraph paragraph = new Paragraph("Total Office Payable(BDT) " +totalOfficePayable,
                smallBold);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        preface.add(paragraph);

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated On: " +deviceCurrentDateTime, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                miniBold));


        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void createTable(Paragraph preface, ArrayList<String> nameList, ArrayList<String> totalAmountList, ArrayList<String> selfAmountList, ArrayList<String> officeAmountList)
            throws BadElementException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell c1 = new PdfPCell(new Phrase("Employee Name"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total Amount (BDT)"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Self Amount (BDT)"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("Office Amount (BDT)"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);
        for(int i=0;i< nameList.size();i++){
            table.addCell(nameList.get(i));
            table.addCell(totalAmountList.get(i));
            table.addCell(selfAmountList.get(i));
            table.addCell(officeAmountList.get(i));
        }
        preface.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

package com.nerdcastle.nazmul.mealdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button pdfBtn;
    Spinner spinnerYear;
    TextView totalAmountTV;
    String monthSelected;
    String deviceCurrentDateTime;
    String totalOfficePayable;
    ListView reportListView;
    ReportModel reportModel;
    AdapterForReport adapterForReport;
    String monthForCheck;
    private ArrayList<ReportModel> reportList;
    String token;
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
        token=getIntent().getStringExtra("Token");
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
            case R.id.mealSubmissionScreen:
                Intent goToMealSubmissionScreenIntent = new Intent(getApplicationContext(),
                        MealSubmissionActivity.class);
                goToMealSubmissionScreenIntent.putExtra("Token",token);
                startActivity(goToMealSubmissionScreenIntent);
                return true;
            case R.id.emailReport:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                startActivity(emailIntent);

                return true;

            case R.id.showReport:
                try {
                    Uri readUri = Uri.parse(Environment.getExternalStorageDirectory() + "/MealManagementApp/");
                    Intent readIntent = new Intent(Intent.ACTION_VIEW);
                    readIntent.setDataAndType(readUri, "resource/folder");
                    startActivity(readIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Your file explorer not working properly!Download ES file explorer", Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initialize() {
        pdfBtn= (Button) findViewById(R.id.pdfBtn);
        totalAmountTV = (TextView) findViewById(R.id.totalAmountTV);
        reportListView = (ListView) findViewById(R.id.totalReportListView);
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        final int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        spinnerMonth = (Spinner) findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.month, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.post(new Runnable() {
            @Override
            public void run() {
                spinnerMonth.setSelection(currentMonth - 1);
            }
        });
        spinnerMonth.setSelection(adapter.getPosition(String.valueOf(currentMonth)));
        spinnerMonth.setAdapter(adapter);
        spinnerYear = (Spinner) findViewById(R.id.yearSpinner);
        ArrayAdapter<CharSequence> priceadapter = ArrayAdapter.createFromResource(
                getBaseContext(), R.array.year, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.post(new Runnable() {
            @Override
            public void run() {
                spinnerYear.setSelection(currentYear - 2015);
            }
        });
        spinnerYear.setAdapter(priceadapter);
    }

    public void getData(View view) {
        String selectedYear = String.valueOf(spinnerYear.getSelectedItem());
        int selectedMonthAmount = spinnerMonth.getSelectedItemPosition() + 1;
        monthSelected = spinnerMonth.getSelectedItem().toString();
        String selectedMonth = String.valueOf(selectedMonthAmount);

        if(adapterForReport==null)
        {
            getTotalAmount(selectedMonth, selectedYear);
            getReport(selectedMonth, selectedYear);
        }else if(!selectedMonth.equals(monthForCheck)){
            getTotalAmount(selectedMonth, selectedYear);
            getReport(selectedMonth, selectedYear);
        }

    }

    private void getReport(final String selectedMonth, final String selectedYear) {
        monthForCheck=selectedMonth;
        reportList=new ArrayList<>();
        String urlToGetTotalAmount = "http://dotnet.nerdcastlebd.com/meal/api/meal/GetMonthlyBill?month=" + selectedMonth + "&year=" + selectedYear;
        JsonArrayRequest requestToGetReport = new JsonArrayRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        String name = response.getJSONObject(i).getString("EmployeeName");
                        String total = response.getJSONObject(i).getString("TotalAmount");
                        String selfAmount = response.getJSONObject(i).getString("SelfAmount");
                        String officeAmount = response.getJSONObject(i).getString("OfficeAmount");
                        String employeeID = response.getJSONObject(i).getString("EmployeeId");
                        reportModel=new ReportModel(name,total,selfAmount,officeAmount,employeeID);
                        reportList.add(reportModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapterForReport = new AdapterForReport( reportList,getBaseContext());
                reportListView.setAdapter(adapterForReport);
                reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String employeeId=reportList.get(position).getEmployeeId();
                        String name=reportList.get(position).getName();
                        Intent detailsReportIntent=new Intent(getApplicationContext(),DetailsReportActivity.class);
                        detailsReportIntent.putExtra("EmployeeId",employeeId);
                        detailsReportIntent.putExtra("Name",name);
                        detailsReportIntent.putExtra("Month",selectedMonth);
                        detailsReportIntent.putExtra("Year",selectedYear);
                        detailsReportIntent.putExtra("token",token);
                        startActivity(detailsReportIntent);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetReport);
    }

    private void getTotalAmount(String selectedMonth, String selectedYear) {

        String urlToGetTotalAmount = "http://dotnet.nerdcastlebd.com/meal/api/meal/GetMonthlyTotalOfficeAmount?month=" + selectedMonth + "&year=" + selectedYear;
        StringRequest requestToGetTotalAmount = new StringRequest(Request.Method.GET, urlToGetTotalAmount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                totalOfficePayable = response;
                totalAmountTV.setVisibility(View.VISIBLE);
                pdfBtn.setVisibility(View.VISIBLE);
                totalAmountTV.setText("Total Office Payable " + response + " tk");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    String msg = "Request Timed Out, Pls try again";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    String msg = "No internet Access, Check your internet connection.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        });
        AppController.getInstance().addToRequestQueue(requestToGetTotalAmount);
    }

    public void createPdf(View view) {
        generatePdf();
        Toast.makeText(getBaseContext(),"Saved Successfully",Toast.LENGTH_LONG).show();
    }

    private void generatePdf() {
        try {
            Document document = new Document();
            String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            deviceCurrentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            File myDir = new File(root + "/MealManagementAppLication");
            myDir.mkdirs();
            String fileName = "MonthlyReportof" + monthSelected +".pdf";
            File file = new File(myDir, fileName);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addTitlePage(document, deviceCurrentDateTime, totalOfficePayable, monthSelected, reportList);
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

    private static void addTitlePage(Document document, String deviceCurrentDateTime, String totalOfficePayable, String mnthSelected, ArrayList<ReportModel> reportList)
            throws DocumentException {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Monthly Report Of " + mnthSelected, catFont));
        addEmptyLine(preface, 1);
        createTable(preface, reportList);
        addEmptyLine(preface, 3);
        Paragraph paragraph = new Paragraph("Total Office Payable(BDT) " + totalOfficePayable,
                smallBold);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        preface.add(paragraph);
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Report generated On: " + deviceCurrentDateTime,
                miniBold));
        document.add(preface);
    }

    private static void createTable(Paragraph preface, ArrayList<ReportModel> reportList)
            throws BadElementException {
        PdfPTable table = new PdfPTable(4);
        PdfPCell cell = new PdfPCell(new Phrase("Employee Name"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Total Amount (BDT)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Self Amount (BDT)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Office Amount (BDT)"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.setHeaderRows(1);
        for (int i = 0; i < reportList.size(); i++) {
            table.addCell(reportList.get(i).getName());
            table.addCell(reportList.get(i).getTotal());
            table.addCell(reportList.get(i).getSelfAmount());
            table.addCell(reportList.get(i).getOfficeAmount());
        }
        preface.add(table);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

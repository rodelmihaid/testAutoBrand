import pdfplumber
import re
import mysql.connector
import pandas as pd
from tkinter import Tk
from tkinter import filedialog


# Functia pentru extragerea numarului de factura din PDF
def extract_invoice_number(pdf_path):
    with pdfplumber.open(pdf_path) as pdf:
        all_text = ''
        # Extrag textul din fiecare pagina a PDF-ului
        for page in pdf.pages:
            all_text += page.extract_text()

    # Folosesc un regex pentru a gasi numarul de factura
    match = re.search(r'Nr\. factura\s*(\d+)', all_text)

    if match:
        invoice_number = match.group(1)
        print(f"Numarul de factura extras: {invoice_number}")
        return invoice_number
    else:
        print("Nu s-a gasit un număr de factura.")
        return None


# Functia pentru conectarea la baza de date MySQL
def create_connection():
    conn = mysql.connector.connect(
        host='localhost',
        user='root',
        password='PAROLATA',
        database='test_autobrand'  # Schimba datetele acestea cu datele tale de conectare
    )
    return conn


# Functia pentru interogarea bazei de date și generarea CSV-ului
def query_database_and_generate_csv(invoice_number, conn):
    cursor = conn.cursor()

    # Interogare SQL folosind DISTINCT pentru a evita dublurile
    query = "SELECT DISTINCT * FROM comenzi WHERE numar_factura = %s"
    cursor.execute(query, (invoice_number,))
    rows = cursor.fetchall()

    # Obtin capetele de tabel
    columns = [description[0] for description in cursor.description]

    if rows:
        # Convertesc rezultatele intr-un DataFrame pandas
        df = pd.DataFrame(rows, columns=columns)

        # Salvez datele într-un fisier CSV
        csv_output_path = f"C:/Users/User/Desktop/testAutoBrand/comenzi_factura_{invoice_number}.csv"
        df.to_csv(csv_output_path, index=False)
        print(f"Fișierul CSV a fost generat: {csv_output_path}")
    else:
        print(f"Nu s-au găsit comenzi pentru numărul de factură {invoice_number}.")


# Functia pentru a deschide un dialog și a selecta un fisier PDF
def select_pdf_file():
    root = Tk()
    root.withdraw()  # Ascund fereastra principala Tkinter
    file_path = filedialog.askopenfilename(title="Selectează fișierul PDF", filetypes=[("PDF files", "*.pdf")])
    return file_path


# Functia principala
def main():
    # Deschid un dialog pentru a selecta fisierul PDF
    pdf_path = select_pdf_file()

    if pdf_path:
        # 1. Extrag numarul de factura din PDF
        invoice_number = extract_invoice_number(pdf_path)

        if invoice_number:
            # 2. Creez conexiunea la baza de date MySQL
            conn = create_connection()

            # 3. Interoghez baza de date și generam fisierul CSV
            query_database_and_generate_csv(invoice_number, conn)

            # Inchid conexiunea
            conn.close()
        else:
            print("Numarul de factura nu a fost gasit în PDF.")
    else:
        print("Nu a fost selectat niciun fisier.")


if __name__ == "__main__":
    main()

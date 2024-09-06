import pdfplumber
import pandas as pd
import re
from tkinter import Tk
from tkinter import filedialog


def select_pdf_file():
    # Inițializam interfata tkinter
    root = Tk()
    root.withdraw()
    # Deschidem dialogul pentru a selecta fisierul PDF
    file_path = filedialog.askopenfilename(title="Selectează fișierul PDF", filetypes=[("PDF files", "*.pdf")])
    return file_path


def extract_data_from_pdf(pdf_path):
    with pdfplumber.open(pdf_path) as pdf:
        all_text = ''
        # Extragem textul din fiecare pagina
        for page in pdf.pages:
            all_text += page.extract_text()

    # Inlocuim toate mentiunile de tip "Pagina X din Y" pentru a evita intreruperile și eliminam alte texte neimportante
    cleaned_text = re.sub(r'Pagina\s+\d+\s+din\s+\d+', '', all_text)
    cleaned_text = re.sub(r'TOTAL.*\n?', '', cleaned_text)
    cleaned_text = re.sub(r'DETALIEREA TVA.*\n?', '', cleaned_text)

    # Cautam liniile relevante cu articole (Pret Unitar, Cantitate, Denumire, Cod TVA, Valoare neta)
    pattern = re.compile(r'(\d+)\s+(.+?)\s+(\d+\.\d{4})\s+RON\s+(\d+\.\d{2})\s+H87\s+19\.00\s+(\d+\.\d{2})')

    matches = pattern.findall(cleaned_text)

    # Cream o lista de dictionare cu articolele gasite
    data = []
    for match in matches:
        linie, denumire, pret_unitar, cantitate, valoare_neta = match
        data.append({
            "Linia": int(linie),
            "Denumire": denumire.strip(),
            "Pret Unitar": float(pret_unitar),
            "Cantitate": float(cantitate),
            "Valoare Neta": float(valoare_neta)
        })

    # Convertim datele într-un DataFrame pandas
    df = pd.DataFrame(data)

    # Salvam datele intr-un fisier CSV în folderul specificat de mine
    csv_output_path = r"C:\Users\User\Desktop\testAutoBrand\articole_extrase.csv"
    df.to_csv(csv_output_path, index=False, encoding='utf-8')

    # Afișam un mesaj de confirmare
    print(f"Fișierul CSV a fost salvat la: {csv_output_path}")

    return csv_output_path


# Deschidem fereastra de selecție a fisierului PDF
pdf_path = select_pdf_file()

# Dacă un fisier a fost selectat, rulam functia de extragere a datelor
if pdf_path:
    csv_file = extract_data_from_pdf(pdf_path)
else:
    print("Nu a fost selectat niciun fisier.")

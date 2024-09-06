import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/product';

  constructor(private http: HttpClient) {}

  getProductData(productCode: string): Observable<any> {
    return this.http.get(`${this.apiUrl}?productCode=${productCode}`);
  }
}

// src/app/components/product/product.component.ts
import { Component } from '@angular/core';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
})
export class ProductComponent {
  product: any;
  productCode: string = '';

  constructor(private productService: ProductService) {}

  searchProduct() {
    this.productService.getProductData(this.productCode).subscribe(
      (data) => (this.product = data),
      (error) => console.error(error)
    );
  }
}

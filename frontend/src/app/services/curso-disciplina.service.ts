import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CursoDisciplina,
  CriarCursoDisciplinaRequest
} from '../models/curso-disciplina.model';

@Injectable({
  providedIn: 'root'
})
export class CursoDisciplinaService {

  private readonly apiUrl = '/api/curso-disciplinas';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<CursoDisciplina[]> {
    return this.http.get<CursoDisciplina[]>(this.apiUrl);
  }

  associar(request: CriarCursoDisciplinaRequest): Observable<CursoDisciplina> {
    return this.http.post<CursoDisciplina>(this.apiUrl, request);
  }
}
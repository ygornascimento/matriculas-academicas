import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Curso, CriarCursoRequest } from '../models/curso.model';

@Injectable({
  providedIn: 'root'
})
export class CursoService {

  private readonly apiUrl = '/api/cursos';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Curso[]> {
    return this.http.get<Curso[]>(this.apiUrl);
  }

  criar(request: CriarCursoRequest): Observable<Curso> {
    return this.http.post<Curso>(this.apiUrl, request);
  }
}
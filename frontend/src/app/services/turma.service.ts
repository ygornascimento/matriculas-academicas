import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CriarTurmaRequest, Turma } from '../models/turma.model';

@Injectable({
  providedIn: 'root'
})
export class TurmaService {

  private readonly apiUrl = '/api/turmas';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<Turma[]> {
    return this.http.get<Turma[]>(this.apiUrl);
  }

  criar(request: CriarTurmaRequest): Observable<Turma> {
    return this.http.post<Turma>(this.apiUrl, request);
  }

  abrir(id: number): Observable<Turma> {
    return this.http.patch<Turma>(`${this.apiUrl}/${id}/abrir`, {});
  }

  fechar(id: number): Observable<Turma> {
    return this.http.patch<Turma>(`${this.apiUrl}/${id}/fechar`, {});
  }
}
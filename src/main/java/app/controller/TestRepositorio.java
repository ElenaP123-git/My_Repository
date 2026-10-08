package app.controller;

import app.repository.UsuarioRepo;

import java.util.List;
import java.util.Map;

public class TestRepositorio {

    public static void main(String[] args) {
        System.out.println("=== TEST REPOSITORIO DAYNEIRA ===\n");

        UsuarioRepo repo = new UsuarioRepo();

        System.out.println("-- Test 1: Contar usuarios --");
        System.out.println("Total: " + repo.contarUsuarios());

        System.out.println("\n-- Test 2: Insertar usuario --");
        int id = repo.insertar("artista_01", "artista@dayneira.com", "hash_test");
        System.out.println(id != -1 ? "Insertado con id: " + id : "Ya existe");

        System.out.println("\n-- Test 3: existeEmail --");
        System.out.println("artista@dayneira.com: " + repo.existeEmail("artista@dayneira.com"));
        System.out.println("nadie@dayneira.com:   " + repo.existeEmail("nadie@dayneira.com"));

        System.out.println("\n-- Test 4: buscarPorEmail --");
        var u = repo.buscarPorEmail("artista@dayneira.com");
        System.out.println(u != null ? "Encontrado: " + u : "No encontrado");

        System.out.println("\n-- Test 5: listarTodos --");
        List<Map<String, String>> todos = repo.listarTodos();
        System.out.println("Total: " + todos.size());
        todos.forEach(fila -> System.out.println("  -> " + fila));

        System.out.println("\n=== FIN ===");
    }
}
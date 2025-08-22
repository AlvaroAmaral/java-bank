## Fin Java

Projeto Java (Gradle) de um domínio bancário simplificado, com carteiras, contas, investimentos e auditoria de movimentações.

### Requisitos
- **JDK 17+** instalado e configurado no PATH/JAVA_HOME
- **Gradle Wrapper** já incluído no repositório (não é necessário instalar Gradle)
- **Lombok** (usado via annotation processor). Em IDEs como IntelliJ/Eclipse, habilite Annotation Processing

### Tecnologias
- Java 17+
- Gradle (via `gradlew`)
- Lombok
- JUnit 5

### Estrutura do projeto
```
java-bank/
  ├─ src/
  │  ├─ main/java/dio/
  │  │  ├─ exception/              # Exceções de domínio
  │  │  ├─ model/                  # Entidades e serviços (Wallet, AccountWallet, Investment...)
  │  │  ├─ repository/             # Repositórios em memória
  │  │  └─ Main.java               # Ponto de entrada do app
  │  └─ main/resources/
  └─ build.gradle.kts              # Configuração Gradle
```

### Como construir
No Windows PowerShell:
```powershell
./gradlew.bat clean build
```
No Linux/macOS:
```bash
./gradlew clean build
```

### Como rodar os testes
Windows:
```powershell
./gradlew.bat test
```
Linux/macOS:
```bash
./gradlew test
```

### Como executar a aplicação (Main)
O projeto usa apenas o plugin `java` (sem `application`). Você pode executar de duas formas:

1) Pela IDE (recomendado):
   - Abra o projeto
   - Habilite annotation processing (Lombok)
   - Rode a classe `dio.Main`

2) Pela linha de comando, usando as classes compiladas:
   - Compile classes:
     - Windows:
       ```powershell
       ./gradlew.bat classes
       ```
     - Linux/macOS:
       ```bash
       ./gradlew classes
       ```
   - Execute o `Main` apontando o classpath para `build/classes` e `build/resources`:
     - Windows (separador `;`):
       ```powershell
       java -cp "build/classes/java/main;build/resources/main" dio.Main
       ```
     - Linux/macOS (separador `:`):
       ```bash
       java -cp "build/classes/java/main:build/resources/main" dio.Main
       ```

Se preferir criar um `run` via Gradle, adicione o plugin `application` e defina `mainClass` em `build.gradle.kts`.

### Comandos úteis
```bash
# Lista tarefas disponíveis
./gradlew tasks

# Ver dependências
./gradlew dependencies
```

### Dicas / Troubleshooting
- **Lombok não funciona na IDE**: instale o plugin Lombok (se aplicável) e habilite Annotation Processing
- **Erro de compilação por JDK**: confirme `java -version` (JDK 17+) e `JAVA_HOME` configurado
- **Sem tarefa de run**: execute via IDE ou use o comando `java -cp` mostrado acima

### Licença
Defina uma licença se necessário (por exemplo, MIT). No momento, nenhuma licença explicitada.



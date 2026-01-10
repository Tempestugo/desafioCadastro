package dominio;

public class Pet {
    private String nome;
    private double idade;
    private double peso;
    private TipoPet Tipopet;
    private String raca;
    private SexoPet sexoPet;
    private Endereco endereco;


    public Pet(String nome, double idade, double peso, TipoPet tipopet, SexoPet sexoPet, Endereco endereco, String raca) {
        this.nome = nome;
        this.idade = idade;
        this.peso = peso;
        Tipopet = tipopet;
        this.sexoPet = sexoPet;
        this.endereco = endereco;
        this.raca = raca;
    }


    public Pet() {

    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Pet(String nomeCompleto, String tipo, String sexo, String idade, String endereco, String raca) {

    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", peso=" + peso +
                '}';
    }


    public TipoPet getTipopet() {
        return Tipopet;
    }

    public void setTipopet(TipoPet tipopet) {
        Tipopet = tipopet;
    }

    public SexoPet getSexoPet() {
        return sexoPet;
    }

    public void setSexoPet(SexoPet sexoPet) {
        this.sexoPet = sexoPet;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getIdade() {
        return idade;
    }

    public void setIdade(double idade) {
        this.idade = idade;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}

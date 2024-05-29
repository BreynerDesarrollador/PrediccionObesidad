package com.PrediccionObesidad.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.converters.ConverterUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/predecirObesidad")
public class PrediccionController {

    private J48 modeloJ48;
    private NaiveBayes modeloNaives;
    private final Instances trainData;

    @Autowired
    public PrediccionController() throws Exception {
        // Cargar el conjunto de datos de entrenamiento
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("D:\\Users\\bjperez\\Downloads\\OBESIDAD_DATASET.csv");
        trainData = source.getDataSet();
        trainData.setClassIndex(trainData.numAttributes() - 1);
    }

    @PostMapping
    public ResponseEntity<RespuestaPrediccionObesidad> predecirObesidad(@RequestBody DatosObesidad data) {
        RespuestaPrediccionObesidad respuesta = new RespuestaPrediccionObesidad();
        double prediccion = 0, promedio = 0;

        try {
            // Crear instancia de Weka a partir de los datos recibidos
            Instance instance = createWekaInstance(data);
            if (data.getTipoAlgoritmo().equals("J48")) {
                // Entrenar el modelo de árbol de decisión J48
                modeloJ48 = new J48();
                modeloJ48.buildClassifier(trainData);
                // Realizar predicción
                prediccion = modeloJ48.classifyInstance(instance);
                promedio = modeloJ48.distributionForInstance(instance)[(int) prediccion];
            } else {
                modeloNaives = new NaiveBayes();
                modeloNaives.buildClassifier(trainData);
                // Realizar predicción
                prediccion = modeloNaives.classifyInstance(instance);
                promedio = modeloNaives.distributionForInstance(instance)[(int) prediccion];
            }


            // Obtener el nombre de la clase predicha
            String ValorClase = trainData.classAttribute().value((int) prediccion);
            // Mapear la predicción a una respuesta
            data datos = new data();
            datos.setPrediccion(prediccion);
            datos.setPromedio(promedio);
            datos.setValorClase(ValorClase);
            datos.setObeso(validarObesidad(ValorClase));
            respuesta.setDatos(datos);
            respuesta.setExito(true);
        } catch (Exception excepcion) {
            respuesta.setMensaje(excepcion.getMessage());
            respuesta.setExito(false);
        }
        return ResponseEntity.ok(respuesta);
    }

    private Instance createWekaInstance(DatosObesidad data) {
        List<Attribute> attributes = new ArrayList<>(trainData.numAttributes());
        for (int i = 0; i < trainData.numAttributes(); i++) {
            attributes.add(trainData.attribute(i));
        }

        Instance instance = new DenseInstance(trainData.numAttributes());
        instance.setDataset(trainData);

        // Asignar valores a los atributos de la instancia
        instance.setValue(attributes.get(0), data.getEdad());
        instance.setValue(attributes.get(1), data.getGenero());
        instance.setValue(attributes.get(2), data.getAltura());
        instance.setValue(attributes.get(3), data.getPeso());
        instance.setValue(attributes.get(4), data.getfrecuencia_alcohol());
        instance.setValue(attributes.get(5), data.getconsumo_alimento_calorico());
        instance.setValue(attributes.get(6), data.getcantidad_verdura());
        instance.setValue(attributes.get(7), data.getnum_comida_dia());
        instance.setValue(attributes.get(8), data.getcontrola_calorias());
        instance.setValue(attributes.get(9), data.getfuma());
        instance.setValue(attributes.get(10), data.getcant_agua_dia());
        instance.setValue(attributes.get(11), data.getFamily_history_with_overweight());
        instance.setValue(attributes.get(12), data.getfrecuencia_act_fisica());
        //instance.setValue(attributes.get(13), data.getTUE());
        instance.setValue(attributes.get(14), data.getalimento_entre_comida());
        instance.setValue(attributes.get(15), data.getmedio_transporte());

        return instance;
    }

    private String validarObesidad(String valorClase) {
        ArrayList<datosObesidad> datos = new ArrayList<>();
        datos.add(new datosObesidad("Insufficient_Weight", "Bajo peso insalubre"));
        datos.add(new datosObesidad("Normal_Weight", "Peso normal"));
        datos.add(new datosObesidad("Overweight_Level_I", "Sobrepeso nivel I"));
        datos.add(new datosObesidad("Overweight_Level_II", "Sobrepeso nivel II"));
        datos.add(new datosObesidad("Obesity_Type_I", "Obesidad tipo I"));
        datos.add(new datosObesidad("Obesity_Type_II", "Obesidad tipo II"));
        datos.add(new datosObesidad("Obesity_Type_III", "Obesidad tipo III"));
        String tempRes = "";
        for (datosObesidad item : datos) {
            if (item.getNombre().toLowerCase().equals(valorClase.toLowerCase()))
                tempRes = item.getValor();
        }
        return tempRes;
    }

    class datosObesidad {
        private String nombre;
        private String valor;

        public datosObesidad(String nombre, String valor) {
            this.nombre = nombre;
            this.valor = valor;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }
    }
}

